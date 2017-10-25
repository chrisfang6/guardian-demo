package net.chris.demo.guardian.model.inject

import dagger.Component
import net.chris.demo.guardian.ListFragment
import net.chris.demo.guardian.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ListModule::class))
abstract class MainComponent {

    abstract fun inject(mainActivity: MainActivity)

    abstract fun inject(fragment: ListFragment)

    companion object {

        internal var instance: MainComponent? = DaggerMainComponent.builder().build()

        private fun getInstance(): MainComponent? {
            if (instance == null) {
                synchronized(MainComponent::class.java) {
                    if (instance == null) {
                        instance = DaggerMainComponent.builder().build()
                    }
                }
            }
            return instance
        }

        fun inject(mainActivity: MainActivity) = getInstance()?.inject(mainActivity)

        fun inject(fragment: ListFragment) = getInstance()?.inject(fragment)

        fun unInject() {
            instance = null
        }
    }

}
