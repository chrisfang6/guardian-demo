package net.chris.demo.guardian.model.inject;

import net.chris.demo.guardian.BuildConfig;
import net.chris.demo.guardian.MainService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public abstract class NetworkComponent {

    public abstract void inject(MainService service);

    private static NetworkComponent instance;

    public static final NetworkComponent getInstance() {
        if (instance == null) {
            synchronized (NetworkComponent.class) {
                if (instance == null) {
                    instance = DaggerNetworkComponent.builder()
                            .networkModule(new NetworkModule(BuildConfig.REQ_ENDPOINT, BuildConfig.DEBUG))
                            .build();
                }
            }
        }
        return instance;
    }

    public static final void uninject() {
        instance = null;
    }

}
