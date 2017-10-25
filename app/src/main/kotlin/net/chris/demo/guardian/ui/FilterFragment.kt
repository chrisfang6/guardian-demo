package net.chris.demo.guardian.ui

import android.app.Dialog
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.trello.rxlifecycle2.components.support.RxDialogFragment
import net.chris.demo.guardian.R
import net.chris.demo.guardian.ui.model.ConcreteCommand
import net.chris.demo.guardian.ui.model.FilterViewModel

class FilterFragment : RxDialogFragment() {

    private var recycler: RecyclerView? = null

    private var filterViewModel: FilterViewModel? = null

    private val onItemTouchListener = object : RecyclerView.SimpleOnItemTouchListener() {
        private val gestureDetector = GestureDetectorCompat(context,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        val childView = recycler?.findChildViewUnder(e.x, e.y)
                        if (childView != null) {
                            ConcreteCommand.create(filterViewModel?.getReceiverItemClicked(recycler?.getChildAdapterPosition(childView) ?: -1)).execute()
                        }
                        return true
                    }
                })

        override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
            gestureDetector.onTouchEvent(e)
            return false
        }
    }

    private val itemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            val interval = resources.getDimensionPixelOffset(R.dimen.dimen_recycler_item_section_interval)
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = interval
            }
            outRect.bottom = interval
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var view: View = createView()
        initCustomView(view)
        return AlertDialog.Builder(context, R.style.Dialog)
                .setView(view)
                .setCancelable(true)
                .create()
    }

    fun initCustomView(view: View) {
        val toolbar: Toolbar? = view.findViewById(R.id.fragment_filter_toolbar)
        toolbar?.inflateMenu(R.menu.menu_filter_dialog)
        toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_done -> {
                    ConcreteCommand.create(filterViewModel?.receiverSectionSelected).execute()
                    dismiss()
                }
                R.id.action_clear -> ConcreteCommand.create(filterViewModel?.receiverClearSection).execute()
            }
            true
        }
        recycler = view.findViewById(R.id.fragment_filter_section_recycler)
        recycler?.adapter = filterViewModel?.getAdapter()
        recycler?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler?.addItemDecoration(itemDecoration)
        recycler?.addOnItemTouchListener(onItemTouchListener)
    }

    override fun onDestroyView() {
        recycler?.removeOnItemTouchListener(onItemTouchListener)
        recycler?.removeItemDecoration(itemDecoration)
        recycler?.adapter = null
        filterViewModel = null
        super.onDestroyView()
    }

    private fun createView(): View {
        return View.inflate(context, rootView(), null)
    }

    private fun rootView(): Int {
        return R.layout.layout_filter
    }

    companion object {
        fun create(filterViewModel: FilterViewModel): FilterFragment {
            val fragment = FilterFragment()
            fragment.filterViewModel = filterViewModel
            return fragment
        }
    }
}
