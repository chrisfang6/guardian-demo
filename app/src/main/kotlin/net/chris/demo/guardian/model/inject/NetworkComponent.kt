package net.chris.demo.guardian.model.inject

import dagger.Component
import net.chris.demo.guardian.BuildConfig
import net.chris.demo.guardian.MainService
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
abstract class NetworkComponent {

    abstract fun inject(service: MainService)

    companion object {

        internal var instance: NetworkComponent? = DaggerNetworkComponent.builder()
                .networkModule(NetworkModule(BuildConfig.REQ_ENDPOINT, BuildConfig.DEBUG))
                .build()

        private fun getInstance(): NetworkComponent? {
            if (instance == null) {
                synchronized(NetworkComponent::class.java) {
                    if (instance == null) {
                        instance = DaggerNetworkComponent.builder()
                                .networkModule(NetworkModule(BuildConfig.REQ_ENDPOINT, BuildConfig.DEBUG))
                                .build()
                    }
                }
            }
            return instance
        }

        fun inject(service: MainService) = getInstance()?.inject(service)

        fun unInject() {
            instance = null
        }
    }

}
