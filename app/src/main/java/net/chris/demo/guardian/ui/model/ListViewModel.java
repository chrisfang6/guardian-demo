package net.chris.demo.guardian.ui.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import net.chris.demo.guardian.IMainInterface;
import net.chris.demo.guardian.MainService;
import net.chris.demo.guardian.database.DatabaseHelper;
import net.chris.demo.guardian.system.DateTimeStruct;
import net.chris.demo.guardian.ui.recycler.ContentListAdapter;

import org.joda.time.format.ISODateTimeFormat;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static net.chris.demo.guardian.database.Columns.ID;
import static net.chris.demo.guardian.database.Columns.WEB_URL;
import static net.chris.demo.guardian.database.SharedPreferencesConstant.DEFAULT_PAGE_SIZE;
import static net.chris.demo.guardian.database.SharedPreferencesConstant.KEY_PAGE_SIZE;
import static net.chris.demo.guardian.database.SharedPreferencesConstant.KEY_SECTION;
import static net.chris.demo.guardian.network.Authentication.FROM_DATE;
import static net.chris.demo.guardian.network.Authentication.PAGE_SIZE;
import static net.chris.demo.guardian.network.Authentication.SECTION;
import static net.chris.demo.guardian.network.Authentication.TO_DATE;

public class ListViewModel {

    private static final String TAG = ListViewModel.class.getSimpleName();

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    private ContentListAdapter adapter;

    private FilterViewModel filterViewModel;

    public ObservableBoolean portrait = new ObservableBoolean(true);

    private ObservableField<String> clickItem = new ObservableField<>(null);
    private OnPropertyChangedCallback clickItemCallback;

    private ObservableBoolean refreshingFinish = new ObservableBoolean(false);
    private OnPropertyChangedCallback refreshingFinishCallback;

    private IMainInterface mainService;
    private boolean isBinded;
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (!isBinded) {
                mainService = IMainInterface.Stub.asInterface(service);
                isBinded = true;
                updateData();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (isBinded) {
                isBinded = false;
                mainService = null;
            }
        }
    };

    public ListViewModel() {
        databaseHelper = new DatabaseHelper();
    }

    public void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        ConcreteCommand.create(getReceiverBindService(context)).execute();
    }

    public void uninit(Context context) {
        filterViewModel.destroy();
        filterViewModel = null;
        ConcreteCommand.create(getReceiverUnbindService(context)).execute();
        unregister();
        if (adapter != null) {
            Cursor cursor = adapter.getCursor();
            if (cursor != null) {
                cursor.close();
            }
            adapter = null;
        }
        sharedPreferences = null;
        databaseHelper = null;
    }

    public CommandReceiver getReceiverBindService(@NonNull final Context context) {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                bindService(context);
            }
        };
    }

    public CommandReceiver getReceiverUnbindService(@NonNull final Context context) {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                unbindService(context);
            }
        };
    }

    public CommandReceiver getReceiverRefresh() {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                refreshContent();
            }
        };
    }

    public CommandReceiver getReceiverFetchMore() {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                loadMoreContent();
            }
        };
    }

    public CommandReceiver getReceiverUpdate() {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                updateAdapter();
            }
        };
    }

    public CommandReceiver getReceiverItemClicked(@NonNull final int position) {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                ContentListAdapter adapter = getAdapter();
                Cursor cursor = adapter.getCursor();
                if (cursor.moveToPosition(position)) {
                    adapter.setSelectedId(cursor.getString(cursor.getColumnIndex(ID)));
                    adapter.notifyDataSetChanged();
                    clickItem.set(cursor.getString(cursor.getColumnIndex(WEB_URL)));
                    clickItem.set(null);
                }
            }
        };
    }

    public CommandReceiver getReceiverUpdatePageSize(int pageSize) {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                if (pageSize > 0 && pageSize < 51) {
                    Observable.defer(() -> Observable.just(sharedPreferences))
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(pref -> pref.edit().putString(KEY_PAGE_SIZE, String.valueOf(pageSize)).apply(),
                                    throwable -> Log.e(TAG, "update page size failed", throwable));
                }
            }
        };
    }

    public void register(@NonNull final RegisterType type,
                         @NonNull final OnPropertyChangedCallback callback) {
        switch (type) {
            case CLICK_ITEM:
                if (clickItemCallback != callback) {
                    clickItem.removeOnPropertyChangedCallback(clickItemCallback);
                    clickItemCallback = callback;
                }
                clickItem.addOnPropertyChangedCallback(clickItemCallback);
                break;
            case REFRESHING_FINISH:
                if (refreshingFinishCallback != callback) {
                    refreshingFinish.removeOnPropertyChangedCallback(refreshingFinishCallback);
                    refreshingFinishCallback = callback;
                }
                refreshingFinish.addOnPropertyChangedCallback(refreshingFinishCallback);
                break;
        }
    }

    public void unregister() {
        clickItem.removeOnPropertyChangedCallback(clickItemCallback);
        refreshingFinish.removeOnPropertyChangedCallback(refreshingFinishCallback);
    }

    private void bindService(@NonNull Context context) {
        context.bindService(new Intent(context, MainService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(@NonNull Context context) {
        context.unbindService(serviceConnection);
    }

    private void refreshContent() {
        getContentRefreshFilter()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(filter -> fetchContent(filter),
                        throwable -> Log.e(TAG, "refresh failed", throwable));
    }

    private void loadMoreContent() {
        Map<String, String> map = new HashMap<>();
        Observable.defer(() -> getPageSize())
                .doOnNext(size -> map.put(PAGE_SIZE, size))
                .flatMap(any -> getSection())
                .doOnNext(section -> {
                    if (section != null && !section.isEmpty()) {
                        map.put(SECTION, section);
                    }
                })
                .flatMap(section -> databaseHelper.getEarliestTime(section))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(dateTime -> {
                    if (!dateTime.isEmpty()) {
                        map.put(TO_DATE, DateTimeStruct.create(dateTime).getDateTime().minusSeconds(1).toString(ISODateTimeFormat.dateTimeNoMillis()));
                    }
                    return map;
                })
                .subscribe(filter -> fetchContent(filter),
                        throwable -> Log.e(TAG, "load more failed", throwable));
    }

    private void updateData() {
        getContentRefreshFilter().subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .map(filter -> mainService.fetchContent(filter))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(fetched -> updateAdapter())
                .observeOn(Schedulers.io())
                .map(any -> mainService.fetchSection())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetched -> getFilterViewModel().updateAdapter(),
                        throwable -> Log.e(TAG, "update data failed", throwable));
    }

    private Observable<Map<String, String>> getContentRefreshFilter() {
        Map<String, String> map = new HashMap<>();
        return Observable.defer(() -> getPageSize())
                .doOnNext(size -> map.put(PAGE_SIZE, size))
                .flatMap(any -> getSection())
                .doOnNext(section -> {
                    if (section != null && !section.isEmpty()) {
                        map.put(SECTION, section);
                    }
                })
                .flatMap(section -> databaseHelper.getLatestTime(section))
                .map(dateTime -> {
                    if (!dateTime.isEmpty()) {
                        map.put(FROM_DATE, DateTimeStruct.create(dateTime).getDateTime().plusSeconds(1).toString(ISODateTimeFormat.dateTimeNoMillis()));
                    }
                    return map;
                });
    }

    private void fetchContent(@Nullable final Map<String, String> filter) {
        Observable.defer(() -> Observable.just(mainService))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(binder -> binder.fetchContent(filter))
                .subscribe(fetched -> updateAdapter(),
                        throwable -> Log.e(TAG, "fetch content failed", throwable));
    }

    public ContentListAdapter getAdapter() {
        if (adapter == null) {
            adapter = new ContentListAdapter();
        }
        return adapter;
    }

    public FilterViewModel getFilterViewModel() {
        if (filterViewModel == null) {
            filterViewModel = new FilterViewModel(databaseHelper,
                    sharedPreferences,
                    new CommandReceiver() {

                        @Override
                        void handleCommand() {
                            refreshContent();
                        }
                    });
        }
        return filterViewModel;
    }

    private void updateAdapter() {
        Observable.defer(() -> getSection())
                .flatMap(section -> Observable.just(databaseHelper.getContentCursor(section)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    refreshingFinish.set(true);
                    refreshingFinish.set(false);
                })
                .subscribe(cursor -> {
                            Cursor old = getAdapter().getCursor();
                            getAdapter().setCursor(cursor);
                            if (old != null) {
                                old.close();
                            }
                            getAdapter().notifyDataSetChanged();
                        },
                        throwable -> Log.e(TAG, "update adapter failed", throwable));
    }

    private Observable<String> getSection() {
        return Observable.defer(() -> Observable.just(sharedPreferences))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(pref -> pref.getString(KEY_SECTION, ""));
    }

    public Observable<String> getPageSize() {
        return Observable.defer(() -> Observable.just(sharedPreferences))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(pref -> pref.getString(KEY_PAGE_SIZE, ""))
                .map(size -> size.isEmpty() ? String.valueOf(DEFAULT_PAGE_SIZE) : size);
    }


    @BindingAdapter("bind:layout_weight")
    public static void setLayoutWeight(View view, int weight) {
        if (view == null) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        layoutParams.weight = weight;
        view.setLayoutParams(layoutParams);
    }

    public enum RegisterType {
        CLICK_ITEM,
        REFRESHING_FINISH,

    }
}
