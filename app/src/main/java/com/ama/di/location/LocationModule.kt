package com.ama.di.location

import androidx.lifecycle.ViewModel
import com.ama.di.viewmodelfactory.ViewModelFactoryModule
import com.ama.di.viewmodelfactory.ViewModelKey
import com.ama.presentation.location.view.LocationFragment
import com.ama.presentation.location.viewmodel.LocationViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class LocationModule {

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    internal abstract fun locationFragment(): LocationFragment

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel::class)
    abstract fun bindViewModel(viewModel: LocationViewModel): ViewModel
}