package net.chris.demo.guardian.model.inject;


import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.chris.demo.guardian.BuildConfig;
import net.chris.lib.network.retrofit.BaseNetworkModuleHelper;
import net.chris.lib.network.retrofit.GzipInterceptor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkModuleHelper extends BaseNetworkModuleHelper {

    public NetworkModuleHelper(@NonNull final HttpUrl baseUrl,
                               @NonNull final boolean debug) {
        super(baseUrl, debug);
    }

    @NonNull
    @Override
    protected List<Interceptor> getExtraNetworkLogInterceptors() {
        List<Interceptor> interceptors = (super.getExtraNetworkLogInterceptors() != null) ?
                super.getExtraNetworkLogInterceptors() : new ArrayList<>();
        if (BuildConfig.DEBUG) {
            interceptors.add(new StethoInterceptor());
        }
        interceptors.add(new GzipInterceptor());
        return interceptors;
    }

    @NonNull
    @Override
    protected List<Interceptor> getExtraInterceptors() {
        List<Interceptor> interceptors = (super.getExtraInterceptors() != null) ?
                super.getExtraInterceptors() : new ArrayList<>();
        interceptors.add(new HttpLoggingInterceptor());
        return interceptors;
    }
}
