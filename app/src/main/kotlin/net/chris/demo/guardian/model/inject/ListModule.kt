package net.chris.demo.guardian.model.inject

import dagger.Module
import dagger.Provides
import net.chris.demo.guardian.ui.model.ListViewModel
import javax.inject.Singleton

@Module
class ListModule {

    @Provides
    @Singleton
    fun provideListViewModel(): ListViewModel = ListViewModel()

}
