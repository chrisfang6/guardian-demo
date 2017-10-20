package net.chris.demo.guardian.model.inject;

import net.chris.demo.guardian.ui.model.ListViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ListModule {

    @Provides
    @Singleton
    protected ListViewModel provideListViewModel() {
        return new ListViewModel();
    }

}
