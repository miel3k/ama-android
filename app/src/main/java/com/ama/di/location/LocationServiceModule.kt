package com.ama.di.location

import com.ama.presentation.location.service.LocationForegroundService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LocationServiceModule {

    @ContributesAndroidInjector
    internal abstract fun locationForegroundService(): LocationForegroundService
}