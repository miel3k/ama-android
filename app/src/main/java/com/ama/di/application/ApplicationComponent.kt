package com.ama.di.application

import android.content.Context
import com.ama.AmaApplication
import com.ama.di.configuration.ConfigurationModule
import com.ama.di.location.LocationModule
import com.ama.di.location.LocationServiceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        ConfigurationModule::class,
        LocationModule::class,
        LocationServiceModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<AmaApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}