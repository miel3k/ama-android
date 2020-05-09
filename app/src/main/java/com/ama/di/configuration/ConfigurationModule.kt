package com.ama.di.configuration

import androidx.lifecycle.ViewModel
import com.ama.presentation.configuration.view.ConfigurationFragment
import com.ama.presentation.configuration.viewmodel.ConfigurationViewModel
import com.ama.di.viewmodelfactory.ViewModelFactoryModule
import com.ama.di.viewmodelfactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ConfigurationModule {

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    internal abstract fun configurationFragment(): ConfigurationFragment

    @Binds
    @IntoMap
    @ViewModelKey(ConfigurationViewModel::class)
    abstract fun bindViewModel(viewModel: ConfigurationViewModel): ViewModel
}