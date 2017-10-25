package net.chris.demo.guardian.model.inject


import com.facebook.stetho.okhttp3.StethoInterceptor
import net.chris.demo.guardian.BuildConfig
import net.chris.lib.network.retrofit.BaseNetworkModuleHelper
import net.chris.lib.network.retrofit.GzipInterceptor
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*

class NetworkModuleHelper(baseUrl: HttpUrl,
                          debug: Boolean) : BaseNetworkModuleHelper(baseUrl, debug) {

    override fun getExtraNetworkLogInterceptors(): List<Interceptor> {
        val interceptors = super.getExtraNetworkLogInterceptors() ?: ArrayList()
        if (BuildConfig.DEBUG) {
            interceptors.add(StethoInterceptor())
        }
        interceptors.add(GzipInterceptor())
        return interceptors
    }

    override fun getExtraInterceptors(): List<Interceptor> {
        val interceptors = super.getExtraInterceptors() ?: ArrayList()
        interceptors.add(HttpLoggingInterceptor())
        return interceptors
    }
}
