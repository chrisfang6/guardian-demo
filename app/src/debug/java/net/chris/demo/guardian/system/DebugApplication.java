package net.chris.demo.guardian.system;

import com.facebook.stetho.Stetho;

import net.chris.tool.debug.library.DebugHelper;

public class DebugApplication extends BaseApplication {

    private DebugHelper debugHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        debugHelper = new DebugHelper();
//        debugHelper.enableStrictMode();
        debugHelper.installLeakCanary(this);
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onTerminate() {
        debugHelper.uninstallLeakCanary(this);
        super.onTerminate();
    }
}
