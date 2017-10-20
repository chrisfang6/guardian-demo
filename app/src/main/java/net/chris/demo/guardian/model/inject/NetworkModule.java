package net.chris.demo.guardian.model.inject;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.chris.demo.guardian.network.NetworkService;
import net.chris.lib.network.retrofit.BaseNetworkModuleHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;

@Module
public class NetworkModule {

    private BaseNetworkModuleHelper helper;

    public NetworkModule(@NonNull final HttpUrl baseUrl,
                         @NonNull final boolean debug) {
        helper = new NetworkModuleHelper(baseUrl, debug);
    }

    @Provides
    @Singleton
    protected NetworkService provideNetworkRequester() {
        return helper.getRetrofitBuilder().build().create(NetworkService.class);
    }

    @Provides
    @Singleton
    protected ObjectMapper provideObjectMapper() {
        return helper.getObjectMapper();
    }

}
