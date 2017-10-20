package net.chris.demo.guardian.ui.model;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import net.chris.demo.guardian.database.DatabaseHelper;
import net.chris.demo.guardian.ui.recycler.SectionListAdapter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static net.chris.demo.guardian.database.Columns.ID;
import static net.chris.demo.guardian.database.SharedPreferencesConstant.KEY_SECTION;

public class FilterViewModel {

    private static final String TAG = FilterViewModel.class.getSimpleName();

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private CommandReceiver commandReceiver;

    private SectionListAdapter adapter;

    public FilterViewModel(@NonNull final DatabaseHelper databaseHelper,
                           @NonNull final SharedPreferences sharedPreferences,
                           @NonNull final CommandReceiver commandReceiver) {
        this.databaseHelper = databaseHelper;
        this.sharedPreferences = sharedPreferences;
        this.commandReceiver = commandReceiver;
    }

    public void destroy() {
        databaseHelper = null;
        sharedPreferences = null;
        commandReceiver = null;
        if (adapter != null) {
            Cursor cursor = adapter.getCursor();
            if (cursor != null) {
                cursor.close();
            }
            adapter = null;
        }
    }

    public SectionListAdapter getAdapter() {
        if (adapter == null) {
            adapter = new SectionListAdapter();
        }
        return adapter;
    }

    public void updateAdapter() {
        Observable.defer(() -> Observable.just(databaseHelper.getSectionCursor()))
                .subscribeOn(Schedulers.io())
                .doOnNext(cursor -> getAdapter().setSelectedId(sharedPreferences.getString(KEY_SECTION, null)))
                .observeOn(AndroidSchedulers.mainThread())
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

    public CommandReceiver getReceiverItemClicked(int position) {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                SectionListAdapter adapter = getAdapter();
                Cursor cursor = adapter.getCursor();
                if (cursor.moveToPosition(position)) {
                    adapter.setSelectedId(cursor.getString(cursor.getColumnIndex(ID)));
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    public CommandReceiver getReceiverSectionSelected() {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                SectionListAdapter adapter = getAdapter();
                String sectionId = adapter.getSelectedId();
                if (sectionId != null) {
                    Observable.defer(() -> Observable.just(sectionId))
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnNext(id -> sharedPreferences.edit().putString(KEY_SECTION, id).apply())
                            .subscribe(any -> ConcreteCommand.create(commandReceiver).execute(),
                                    throwable -> Log.e(TAG, "change section failed", throwable));
                } else {
                    Observable.defer(() -> Observable.just(sharedPreferences))
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnNext(preference -> preference.edit().remove(KEY_SECTION).apply())
                            .subscribe(any -> ConcreteCommand.create(commandReceiver).execute(),
                                    throwable -> Log.e(TAG, "remove section failed", throwable));
                }
            }
        };
    }

    public CommandReceiver getReceiverClearSection() {
        return new CommandReceiver() {
            @Override
            void handleCommand() {
                SectionListAdapter adapter = getAdapter();
                adapter.setSelectedId(null);
                adapter.notifyDataSetChanged();
            }
        };
    }
}
