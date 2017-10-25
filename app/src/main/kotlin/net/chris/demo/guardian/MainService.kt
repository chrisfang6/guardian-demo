package net.chris.demo.guardian

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import net.chris.demo.guardian.model.inject.NetworkComponent
import net.chris.demo.guardian.network.Authentication.Companion.API_KEY
import net.chris.demo.guardian.network.Authentication.Companion.FORMAT
import net.chris.demo.guardian.network.Authentication.Companion.FORMAT_VALUE_JSON
import net.chris.demo.guardian.network.NetworkService
import javax.inject.Inject

class MainService : Service() {

    @Inject
    lateinit var networkService: NetworkService

    private val binder = MainBinder()

    override fun onCreate() {
        super.onCreate()
        NetworkComponent.inject(this)
    }

    override fun onDestroy() {
        NetworkComponent.unInject()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    companion object {
        private val TAG = MainService::class.java.simpleName
    }

    inner class MainBinder : Binder() {

        @Synchronized
        fun fetchContent(filter: Map<*, *>?): Boolean {
            val map = HashMap<String, String>()
            map.putAll(filter?.checkItemsAre() ?: HashMap())
            map.put(API_KEY, BuildConfig.GUARDIAN_API_KEY)
            map.put(FORMAT, FORMAT_VALUE_JSON)
            try {
                networkService.fetchContent(map).execute().body()?.response?.save()
                return true
            } catch (e: Exception) {
                Log.e(TAG, "fetch content failed", e)
            }
            return false
        }

        @Synchronized
        fun fetchSection(): Boolean {
            val map = HashMap<String, String>()
            map.put(API_KEY, BuildConfig.GUARDIAN_API_KEY)
            map.put(FORMAT, FORMAT_VALUE_JSON)
            try {
                networkService.fetchSection(map).execute().body()?.response?.save()
                return true
            } catch (e: Exception) {
                Log.e(TAG, "fetch section failed", e)
            }
            return false
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any, reified V : Any> Map<*, *>.checkItemsAre() =
            if (all { it.key is T && it.value is V })
                this as Map<T, V>
            else null
}