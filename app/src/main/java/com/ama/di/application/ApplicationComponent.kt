package com.ama.di.application

import android.content.Context
import com.ama.AmaApplication
import com.ama.di.configuration.ConfigurationModule
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
        ConfigurationModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<AmaApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}