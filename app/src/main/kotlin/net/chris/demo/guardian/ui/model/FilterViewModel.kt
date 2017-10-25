package net.chris.demo.guardian.ui.model

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.chris.demo.guardian.database.Columns.Companion.ID
import net.chris.demo.guardian.database.DatabaseHelper
import net.chris.demo.guardian.database.SharedPreferencesConstant.Companion.KEY_SECTION
import net.chris.demo.guardian.ui.recycler.SectionListAdapter

class FilterViewModel(private var databaseHelper: DatabaseHelper,
                      private var sharedPreferences: SharedPreferences,
                      private var commandReceiver: CommandReceiver) {

    private var adapter: SectionListAdapter? = null

    val receiverSectionSelected: CommandReceiver
        get() = object : CommandReceiver() {
            override fun handleCommand() {
                val adapter = getAdapter()
                val sectionId = adapter.selectedId
                if (sectionId != null) {
                    Observable.defer { Observable.just(sectionId) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnNext { id -> sharedPreferences.edit().putString(KEY_SECTION, id).apply() }
                            .subscribe({ ConcreteCommand.create(commandReceiver).execute() }
                            ) { throwable -> Log.e(TAG, "change section failed", throwable) }
                } else {
                    Observable.defer { Observable.just(sharedPreferences) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnNext { preference -> preference.edit().remove(KEY_SECTION).apply() }
                            .subscribe({ ConcreteCommand.create(commandReceiver).execute() }
                            ) { throwable -> Log.e(TAG, "remove section failed", throwable) }
                }
            }
        }

    val receiverClearSection: CommandReceiver
        get() = object : CommandReceiver() {
            override fun handleCommand() {
                val adapter = getAdapter()
                adapter.selectedId = null
                adapter.notifyDataSetChanged()
            }
        }

    fun destroy() {
        adapter?.cursor?.close()
        adapter = null
    }

    fun getAdapter(): SectionListAdapter {
        if (adapter == null) {
            adapter = SectionListAdapter()
        }
        return adapter as SectionListAdapter
    }

    fun updateAdapter() {
        Observable.defer { Observable.just(databaseHelper.sectionCursor) }
                .subscribeOn(Schedulers.io())
                .doOnNext { getAdapter().selectedId = sharedPreferences.getString(KEY_SECTION, null) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ cursor ->
                    val old = getAdapter().cursor
                    getAdapter().cursor = cursor
                    old?.close()
                    getAdapter().notifyDataSetChanged()
                }
                ) { throwable -> Log.e(TAG, "update adapter failed", throwable) }
    }

    fun getReceiverItemClicked(position: Int): CommandReceiver {
        return object : CommandReceiver() {
            override fun handleCommand() {
                val adapter = getAdapter()
                val cursor = adapter.cursor
                if (cursor?.moveToPosition(position) == true) {
                    adapter.selectedId = cursor.getString(cursor.getColumnIndex(ID))
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {

        private val TAG = FilterViewModel::class.java.simpleName
    }
}
