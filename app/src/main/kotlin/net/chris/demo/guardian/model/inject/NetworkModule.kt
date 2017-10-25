package net.chris.demo.guardian.model.inject

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import net.chris.demo.guardian.network.NetworkService
import net.chris.lib.network.retrofit.BaseNetworkModuleHelper
import okhttp3.HttpUrl
import javax.inject.Singleton

@Module
class NetworkModule(baseUrl: HttpUrl,
                    debug: Boolean) {

    private var baseUrl = baseUrl

    private var debug = debug

    @Provides
    @Singleton
    fun provideNetworkRequester(helper: BaseNetworkModuleHelper): NetworkService = helper.retrofitBuilder.build().create(NetworkService::class.java)

    @Provides
    @Singleton
    fun provideObjectMapper(helper: BaseNetworkModuleHelper): ObjectMapper = helper.objectMapper

    @Provides
    @Singleton
    fun provideNetworkModuleHelper(): BaseNetworkModuleHelper = NetworkModuleHelper(baseUrl, debug)

}
