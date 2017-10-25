package net.chris.demo.guardian

import android.content.Intent
import android.content.res.Configuration
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.trello.rxlifecycle2.components.support.RxFragment
import kotterknife.bindView
import net.chris.demo.guardian.database.Columns.Companion.WEB_URL
import net.chris.demo.guardian.model.inject.MainComponent
import net.chris.demo.guardian.system.Constant.Companion.ACTION_SHOW_WEB
import net.chris.demo.guardian.ui.model.ConcreteCommand
import net.chris.demo.guardian.ui.model.ListViewModel
import net.chris.demo.guardian.ui.model.ListViewModel.RegisterType.CLICK_ITEM
import net.chris.demo.guardian.ui.model.ListViewModel.RegisterType.REFRESHING_FINISH
import javax.inject.Inject

class ListFragment : RxFragment() {

    @Inject
    lateinit var listViewModel: ListViewModel

    val refreshLayout: SwipeRefreshLayout by bindView(R.id.list_refresh)
    val recycler: RecyclerView by bindView(R.id.list_recycler)

    private val onItemTouchListener = object : RecyclerView.SimpleOnItemTouchListener() {
        private val gestureDetector = GestureDetectorCompat(context,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        val childView = recycler.findChildViewUnder(e.x, e.y)
                        if (childView != null) {
                            ConcreteCommand.create(listViewModel.getReceiverItemClicked(recycler.getChildAdapterPosition(childView))).execute()
                        }
                        return true
                    }
                })

        override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
            gestureDetector.onTouchEvent(e)
            return false
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {

        private var isScrollDown: Boolean = false

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isScrollDown && isLatestCompletedItemVisible(recyclerView)) {
                // If the latest item is visible in the list on the screen, and there are more items, then download more.
                Snackbar.make(recyclerView, "Loading more ...", Snackbar.LENGTH_SHORT).show()
                ConcreteCommand.create(listViewModel.receiverFetchMore).execute()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isScrollDown = dy > 0
        }

        private fun isLatestCompletedItemVisible(recyclerView: RecyclerView): Boolean {
            val manager = recyclerView.layoutManager as LinearLayoutManager
            val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
            val totalItemCount = manager.itemCount
            return lastVisiblePosition == totalItemCount - 1
        }
    }

    private val itemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            val interval = resources.getDimensionPixelOffset(R.dimen.dimen_recycler_interval)
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = interval
            }
            outRect.bottom = interval
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.adapter = listViewModel.getAdapter()
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler.addItemDecoration(itemDecoration)
        recycler.addOnItemTouchListener(onItemTouchListener)
        recycler.addOnScrollListener(onScrollListener)
        refreshLayout.setOnRefreshListener { ConcreteCommand.create(listViewModel.receiverRefresh).execute() }
        listViewModel.register(CLICK_ITEM, object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                @Suppress("UNCHECKED_CAST")
                val webUrl = (sender as ObservableField<String>).get()
                if (webUrl != null) {
                    when (activity.resources.configuration.orientation) {
                        Configuration.ORIENTATION_PORTRAIT -> startActivity(Intent(context, DetailActivity::class.java).putExtra(WEB_URL, webUrl))
                        Configuration.ORIENTATION_LANDSCAPE -> LocalBroadcastManager.getInstance(activity).sendBroadcast(Intent(ACTION_SHOW_WEB).putExtra(WEB_URL, webUrl))
                    }
                }
            }
        })
        listViewModel.register(REFRESHING_FINISH, object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                val finish = (sender as ObservableBoolean).get()
                if (finish) {
                    refreshLayout.isRefreshing = false
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        ConcreteCommand.create(listViewModel.receiverUpdate).execute()
    }

    override fun onDestroyView() {
        listViewModel.unregister()
        refreshLayout.setOnRefreshListener(null)
        recycler.adapter = null
        recycler.removeItemDecoration(itemDecoration)
        recycler.removeOnItemTouchListener(onItemTouchListener)
        recycler.removeOnScrollListener(onScrollListener)
        super.onDestroyView()
    }

}
