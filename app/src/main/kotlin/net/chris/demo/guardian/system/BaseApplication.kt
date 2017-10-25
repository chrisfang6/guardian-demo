package net.chris.demo.guardian.system

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import net.chris.demo.guardian.IMMLeaks

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(FlowConfig.Builder(this).build())
        IMMLeaks.fixFocusedViewLeak(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        FlowManager.destroy()
    }
}
