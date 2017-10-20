package net.chris.demo.guardian.model.inject;

import net.chris.demo.guardian.ListFragment;
import net.chris.demo.guardian.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ListModule.class})
public abstract class MainComponent {

    public abstract void inject(MainActivity mainActivity);

    public abstract void inject(ListFragment fragment);

    private static MainComponent instance;

    public static MainComponent getInstance() {
        if (instance == null) {
            synchronized (MainComponent.class) {
                if (instance == null) {
                    instance = DaggerMainComponent.builder().build();
                }
            }
        }
        return instance;
    }

    public static void uninject() {
        instance = null;
    }

}
