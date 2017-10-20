package net.chris.demo.guardian.ui;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxDialogFragment;

import net.chris.demo.guardian.R;
import net.chris.demo.guardian.ui.model.ConcreteCommand;
import net.chris.demo.guardian.ui.model.FilterViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FilterFragment extends RxDialogFragment {

    @BindView(R.id.fragment_filter_section_recycler)
    RecyclerView recycler;
    @BindView(R.id.fragment_filter_toolbar)
    protected Toolbar toolbar;

    protected Unbinder unbinder;
    private FilterViewModel filterViewModel;

    private RecyclerView.SimpleOnItemTouchListener onItemTouchListener = new RecyclerView.SimpleOnItemTouchListener() {
        private GestureDetectorCompat gestureDetector = new GestureDetectorCompat(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = recycler.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null) {
                            ConcreteCommand.create(filterViewModel.getReceiverItemClicked(recycler.getChildAdapterPosition(childView))).execute();
                        }
                        return true;
                    }
                });

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            gestureDetector.onTouchEvent(e);
            return false;
        }
    };

    private RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            final int interval = getResources().getDimensionPixelOffset(R.dimen.dimen_recycler_item_section_interval);
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = interval;
            }
            outRect.bottom = interval;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = createView();
        final AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.Dialog)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(true);
        toolbar.inflateMenu(R.menu.menu_filter_dialog);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_done:
                    ConcreteCommand.create(filterViewModel.getReceiverSectionSelected()).execute();
                    dismiss();
                    break;
                case R.id.action_clear:
                    ConcreteCommand.create(filterViewModel.getReceiverClearSection()).execute();
                    break;
            }
            return true;
        });
        recycler.setAdapter(filterViewModel.getAdapter());
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(itemDecoration);
        recycler.addOnItemTouchListener(onItemTouchListener);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        recycler.removeOnItemTouchListener(onItemTouchListener);
        recycler.removeItemDecoration(itemDecoration);
        recycler.setAdapter(null);
        filterViewModel = null;
        unbinder.unbind();
        super.onDestroyView();
    }

    @NonNull
    private View createView() {
        final View view = View.inflate(getContext(), rootView(), null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @NonNull
    private int rootView() {
        return R.layout.layout_filter;
    }


    @NonNull
    public static FilterFragment create(@NonNull final FilterViewModel filterViewModel) {
        final FilterFragment fragment = new FilterFragment();
        fragment.filterViewModel = filterViewModel;
        return fragment;
    }
}
