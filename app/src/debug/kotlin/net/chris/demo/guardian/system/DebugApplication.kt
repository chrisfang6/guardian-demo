package net.chris.demo.guardian.system

import com.facebook.stetho.Stetho

import net.chris.tool.debug.library.DebugHelper

class DebugApplication : BaseApplication() {

    private lateinit var debugHelper: DebugHelper

    override fun onCreate() {
        super.onCreate()
        debugHelper = DebugHelper()
        //        debugHelper.enableStrictMode();
        debugHelper.installLeakCanary(this)
        Stetho.initializeWithDefaults(this)
    }

    override fun onTerminate() {
        debugHelper.uninstallLeakCanary(this)
        super.onTerminate()
    }
}
