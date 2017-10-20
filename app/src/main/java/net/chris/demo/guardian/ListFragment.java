package net.chris.demo.guardian;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import net.chris.demo.guardian.model.inject.MainComponent;
import net.chris.demo.guardian.ui.model.ConcreteCommand;
import net.chris.demo.guardian.ui.model.ListViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static net.chris.demo.guardian.database.Columns.WEB_URL;
import static net.chris.demo.guardian.system.Constant.ACTION_SHOW_WEB;
import static net.chris.demo.guardian.ui.model.ListViewModel.RegisterType.CLICK_ITEM;
import static net.chris.demo.guardian.ui.model.ListViewModel.RegisterType.REFRESHING_FINISH;

public class ListFragment extends RxFragment {

    @Inject
    ListViewModel listViewModel;

    @BindView(R.id.list_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.list_recycler)
    RecyclerView recycler;

    private Unbinder unbinder;

    private RecyclerView.SimpleOnItemTouchListener onItemTouchListener = new RecyclerView.SimpleOnItemTouchListener() {
        private GestureDetectorCompat gestureDetector = new GestureDetectorCompat(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = recycler.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null) {
                            ConcreteCommand.create(listViewModel.getReceiverItemClicked(recycler.getChildAdapterPosition(childView))).execute();
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

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        private boolean isScrollDown;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isScrollDown && isLatestCompletedItemVisible(recyclerView)) {
                // If the latest item is visible in the list on the screen, and there are more items, then download more.
                Snackbar.make(recyclerView, "Loading more ...", Snackbar.LENGTH_SHORT).show();
                ConcreteCommand.create(listViewModel.getReceiverFetchMore()).execute();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = (dy > 0) ? true : false;
        }

        private boolean isLatestCompletedItemVisible(RecyclerView recyclerView) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
            int totalItemCount = manager.getItemCount();
            if (lastVisiblePosition == (totalItemCount - 1)) {
                return true;
            } else {
                return false;
            }
        }
    };

    private RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            final int interval = getResources().getDimensionPixelOffset(R.dimen.dimen_recycler_interval);
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = interval;
            }
            outRect.bottom = interval;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainComponent.getInstance().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler.setAdapter(listViewModel.getAdapter());
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(itemDecoration);
        recycler.addOnItemTouchListener(onItemTouchListener);
        recycler.addOnScrollListener(onScrollListener);
        refreshLayout.setOnRefreshListener(() -> ConcreteCommand.create(listViewModel.getReceiverRefresh()).execute());
        listViewModel.register(CLICK_ITEM, new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String webUrl = ((ObservableField<String>) sender).get();
                if (webUrl != null) {
                    switch (getActivity().getResources().getConfiguration().orientation) {
                        case Configuration.ORIENTATION_PORTRAIT:
                            startActivity(new Intent(getContext(), DetailActivity.class).putExtra(WEB_URL, webUrl));
                            break;
                        case Configuration.ORIENTATION_LANDSCAPE:
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ACTION_SHOW_WEB).putExtra(WEB_URL, webUrl));
                            break;
                    }
                }
            }
        });
        listViewModel.register(REFRESHING_FINISH, new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean finish = ((ObservableBoolean) sender).get();
                if (finish) {
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ConcreteCommand.create(listViewModel.getReceiverUpdate()).execute();
    }

    @Override
    public void onDestroyView() {
        listViewModel.unregister();
        refreshLayout.setOnRefreshListener(null);
        recycler.setAdapter(null);
        recycler.removeItemDecoration(itemDecoration);
        recycler.removeOnItemTouchListener(onItemTouchListener);
        recycler.removeOnScrollListener(onScrollListener);
        unbinder.unbind();
        super.onDestroyView();
    }

}
