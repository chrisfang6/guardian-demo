package net.chris.demo.guardian.ui.model

import android.content.*
import android.databinding.Observable.OnPropertyChangedCallback
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.chris.demo.guardian.MainService
import net.chris.demo.guardian.database.Columns.Companion.ID
import net.chris.demo.guardian.database.Columns.Companion.WEB_URL
import net.chris.demo.guardian.database.DatabaseHelper
import net.chris.demo.guardian.database.SharedPreferencesConstant.Companion.DEFAULT_PAGE_SIZE
import net.chris.demo.guardian.database.SharedPreferencesConstant.Companion.KEY_PAGE_SIZE
import net.chris.demo.guardian.database.SharedPreferencesConstant.Companion.KEY_SECTION
import net.chris.demo.guardian.network.Authentication.Companion.FROM_DATE
import net.chris.demo.guardian.network.Authentication.Companion.PAGE_SIZE
import net.chris.demo.guardian.network.Authentication.Companion.SECTION
import net.chris.demo.guardian.network.Authentication.Companion.TO_DATE
import net.chris.demo.guardian.system.DateTimeStruct
import net.chris.demo.guardian.ui.recycler.ContentListAdapter
import org.joda.time.format.ISODateTimeFormat
import java.util.*

class ListViewModel {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    private var adapter: ContentListAdapter? = null

    private var filterViewModel: FilterViewModel? = null

    var portrait = ObservableBoolean(true)

    private val clickItem = ObservableField<String>(null)
    private var clickItemCallback: OnPropertyChangedCallback? = null

    private val refreshingFinish = ObservableBoolean(false)
    private var refreshingFinishCallback: OnPropertyChangedCallback? = null

    private var mainBinder: MainService.MainBinder? = null
    private var isBinded: Boolean = false
    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected")
            if (!isBinded) {
                mainBinder = service as MainService.MainBinder
                isBinded = true
                updateData()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "onServiceDisconnected")
            if (isBinded) {
                isBinded = false
                mainBinder = null
            }
        }
    }

    val receiverRefresh: CommandReceiver
        get() = object : CommandReceiver() {
            override fun handleCommand() {
                refreshContent()
            }
        }

    val receiverFetchMore: CommandReceiver
        get() = object : CommandReceiver() {
            override fun handleCommand() {
                loadMoreContent()
            }
        }

    val receiverUpdate: CommandReceiver
        get() = object : CommandReceiver() {
            override fun handleCommand() {
                updateAdapter()
            }
        }

    private val contentRefreshFilter: Observable<Map<String, String>>
        get() {
            val map = HashMap<String, String>()
            return Observable.defer { pageSize }
                    .doOnNext { size -> map.put(PAGE_SIZE, size) }
                    .flatMap { section }
                    .doOnNext { section ->
                        if (section != null && !section.isEmpty()) {
                            map.put(SECTION, section)
                        }
                    }
                    .flatMap { section -> databaseHelper?.getLatestTime(section) }
                    .map { dateTime ->
                        if (!dateTime.isEmpty()) {
                            map.put(FROM_DATE, DateTimeStruct.create(dateTime).dateTime.plusSeconds(1).toString(ISODateTimeFormat.dateTimeNoMillis()))
                        }
                        map
                    }
        }

    private val section: Observable<String>
        get() = Observable.defer { Observable.just(sharedPreferences) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { pref -> pref.getString(KEY_SECTION, "") }

    val pageSize: Observable<String>
        get() = Observable.defer { Observable.just(sharedPreferences) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { pref -> pref.getString(KEY_PAGE_SIZE, "") }
                .map { size -> if (size.isEmpty()) DEFAULT_PAGE_SIZE.toString() else size }

    fun init(context: Context) {
        databaseHelper = DatabaseHelper()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        ConcreteCommand.create(getReceiverBindService(context)).execute()
    }

    fun uninit(context: Context) {
        filterViewModel?.destroy()
        filterViewModel = null
        ConcreteCommand.create(getReceiverUnbindService(context)).execute()
        unregister()
        adapter?.cursor?.close()
        adapter = null
    }

    private fun getReceiverBindService(context: Context): CommandReceiver {
        return object : CommandReceiver() {
            override fun handleCommand() {
                bindService(context)
            }
        }
    }

    private fun getReceiverUnbindService(context: Context): CommandReceiver {
        return object : CommandReceiver() {
            override fun handleCommand() {
                unbindService(context)
            }
        }
    }

    fun getReceiverItemClicked(position: Int): CommandReceiver {
        return object : CommandReceiver() {
            override fun handleCommand() {
                val adapter = getAdapter()
                val cursor = adapter.cursor
                if (cursor?.moveToPosition(position) == true) {
                    adapter.setSelectedId(cursor.getString(cursor.getColumnIndex(ID)))
                    adapter.notifyDataSetChanged()
                    clickItem.set(cursor.getString(cursor.getColumnIndex(WEB_URL)))
                    clickItem.set(null)
                }
            }
        }
    }

    fun getReceiverUpdatePageSize(pageSize: Int): CommandReceiver {
        return object : CommandReceiver() {
            override fun handleCommand() {
                if (pageSize in 1..50) {
                    Observable.defer { Observable.just(sharedPreferences) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe({ pref -> pref.edit().putString(KEY_PAGE_SIZE, pageSize.toString()).apply() }
                            ) { throwable -> Log.e(TAG, "update page size failed", throwable) }
                }
            }
        }
    }

    fun register(type: RegisterType,
                 callback: OnPropertyChangedCallback) {
        when (type) {
            ListViewModel.RegisterType.CLICK_ITEM -> {
                if (clickItemCallback !== callback) {
                    clickItem.removeOnPropertyChangedCallback(clickItemCallback)
                    clickItemCallback = callback
                }
                clickItem.addOnPropertyChangedCallback(clickItemCallback)
            }
            ListViewModel.RegisterType.REFRESHING_FINISH -> {
                if (refreshingFinishCallback !== callback) {
                    refreshingFinish.removeOnPropertyChangedCallback(refreshingFinishCallback)
                    refreshingFinishCallback = callback
                }
                refreshingFinish.addOnPropertyChangedCallback(refreshingFinishCallback)
            }
        }
    }

    fun unregister() {
        clickItem.removeOnPropertyChangedCallback(clickItemCallback)
        refreshingFinish.removeOnPropertyChangedCallback(refreshingFinishCallback)
    }

    private fun bindService(context: Context) {
        Log.e(TAG, "bindService")
        context.bindService(Intent(context, MainService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService(context: Context) {
        Log.e(TAG, "unbindService")
        context.unbindService(serviceConnection)
    }

    private fun refreshContent() {
        contentRefreshFilter
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ filter -> fetchContent(filter) }
                ) { throwable -> Log.e(TAG, "refresh failed", throwable) }
    }

    private fun loadMoreContent() {
        val map = HashMap<String, String>()
        Observable.defer { pageSize }
                .doOnNext { size -> map.put(PAGE_SIZE, size) }
                .flatMap { section }
                .doOnNext { section ->
                    if (section != null && !section.isEmpty()) {
                        map.put(SECTION, section)
                    }
                }
                .flatMap { section -> databaseHelper.getEarliestTime(section) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map<Map<String, String>> { dateTime ->
                    if (!dateTime.isEmpty()) {
                        map.put(TO_DATE, DateTimeStruct.create(dateTime).dateTime.minusSeconds(1).toString(ISODateTimeFormat.dateTimeNoMillis()))
                    }
                    map
                }
                .subscribe({ filter -> fetchContent(filter) }
                ) { throwable -> Log.e(TAG, "load more failed", throwable) }
    }

    private fun updateData() {
        contentRefreshFilter.subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .flatMap { fetchContentRx(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { updateAdapter() }
                .observeOn(Schedulers.io())
                .flatMap { fetchSectionRx() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ getFilterViewModel().updateAdapter() }
                ) { throwable -> Log.e(TAG, "update data failed", throwable) }
    }

    private fun fetchContent(filter: Map<String, String>?) {
        Observable.defer { fetchContentRx(filter) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ updateAdapter() }
                ) { throwable -> Log.e(TAG, "fetch content failed", throwable) }
    }

    private fun fetchContentRx(filter: Map<String, String>?): Observable<Boolean>? {
        return Observable.just(mainBinder!!).map { (it.fetchContent(filter)) }
    }

    private fun fetchSectionRx(): Observable<Boolean>? {
        return Observable.just(mainBinder!!).map { (it.fetchSection()) }
    }

    fun getAdapter(): ContentListAdapter {
        if (adapter == null) {
            adapter = ContentListAdapter()
        }
        return adapter as ContentListAdapter
    }

    fun getFilterViewModel(): FilterViewModel {
        if (filterViewModel == null) {
            filterViewModel = FilterViewModel(databaseHelper,
                    sharedPreferences,
                    object : CommandReceiver() {
                        override fun handleCommand() {
                            refreshContent()
                        }
                    })
        }
        return filterViewModel as FilterViewModel
    }

    private fun updateAdapter() {
        Observable.defer { section }
                .flatMap { section -> Observable.just(databaseHelper.getContentCursor(section)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate {
                    refreshingFinish.set(true)
                    refreshingFinish.set(false)
                }
                .subscribe({ cursor ->
                    val old = getAdapter().cursor
                    getAdapter().cursor = cursor
                    old?.close()
                    getAdapter().notifyDataSetChanged()
                }
                ) { throwable -> Log.e(TAG, "update adapter failed", throwable) }
    }

    enum class RegisterType {
        CLICK_ITEM,
        REFRESHING_FINISH

    }

    companion object {

        private val TAG = ListViewModel::class.java.simpleName

    }
}
