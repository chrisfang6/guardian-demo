package net.chris.demo.guardian;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import net.chris.demo.guardian.model.inject.NetworkComponent;
import net.chris.demo.guardian.network.NetworkService;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static net.chris.demo.guardian.network.Authentication.API_KEY;
import static net.chris.demo.guardian.network.Authentication.FORMAT;
import static net.chris.demo.guardian.network.Authentication.FORMAT_VALUE_JSON;

public class MainService extends Service {

    private static final String TAG = MainService.class.getSimpleName();

    @Inject
    NetworkService networkService;

    private SharedPreferences sharedPreferences;

    private IMainInterface.Stub binder = new IMainInterface.Stub() {
        @Override
        public boolean fetchContent(Map filter) throws RemoteException {
            Map<String, String> map = new HashMap<>();
            if (filter != null) {
                map.putAll(filter);
            }
            map.put(API_KEY, BuildConfig.GUARDIAN_API_KEY);
            map.put(FORMAT, FORMAT_VALUE_JSON);
            try {
                networkService.fetchContent(map).execute()
                        .body().getResponse().save();
                return true;
            } catch (Exception e) {
                Log.e(TAG, "fetch content failed", e);
            }
            return false;
        }

        @Override
        public boolean fetchSection() throws RemoteException {
            Map<String, String> map = new HashMap<>();
            map.put(API_KEY, BuildConfig.GUARDIAN_API_KEY);
            map.put(FORMAT, FORMAT_VALUE_JSON);
            try {
                networkService.fetchSection(map).execute()
                        .body().getResponse().save();
                return true;
            } catch (Exception e) {
                Log.e(TAG, "fetch section failed", e);
            }
            return false;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkComponent.getInstance().inject(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onDestroy() {
        NetworkComponent.uninject();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
