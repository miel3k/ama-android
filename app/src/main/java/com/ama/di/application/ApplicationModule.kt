package com.ama.di.application

import android.content.Context
import androidx.room.Room
import com.ama.data.AmaDatabase
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.ConfigurationsRepository
import com.ama.data.configurations.local.ConfigurationsLocal
import com.ama.data.configurations.remote.ConfigurationsApi
import com.ama.data.configurations.remote.ConfigurationsRemote
import com.ama.data.events.EventsDataSource
import com.ama.data.events.EventsRepository
import com.ama.data.events.local.EventsLocal
import com.ama.data.locations.LocationsDataSource
import com.ama.data.locations.LocationsRepository
import com.ama.data.locations.local.LocationsLocal
import com.ama.data.locations.remote.LocationsApi
import com.ama.data.locations.remote.LocationsRemote
import com.ama.di.network.NetworkModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class, NetworkModule::class])
object ApplicationModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ConfigurationsRemote

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ConfigurationsLocal

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocationsRemote

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocationsLocal

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class EventsLocal

    @JvmStatic
    @Singleton
    @ConfigurationsRemote
    @Provides
    fun provideConfigurationsRemote(configurationsApi: ConfigurationsApi): ConfigurationsDataSource {
        return ConfigurationsRemote(configurationsApi)
    }

    @JvmStatic
    @Singleton
    @ConfigurationsLocal
    @Provides
    fun provideConfigurationsLocal(
        database: AmaDatabase,
        dispatcher: CoroutineDispatcher
    ): ConfigurationsDataSource {
        return ConfigurationsLocal(database.configurationsDao(), dispatcher)
    }

    @JvmStatic
    @Singleton
    @LocationsRemote
    @Provides
    fun provideLocationsRemote(locationsApi: LocationsApi): LocationsDataSource {
        return LocationsRemote(locationsApi)
    }

    @JvmStatic
    @Singleton
    @LocationsLocal
    @Provides
    fun provideLocationsLocal(
        database: AmaDatabase,
        dispatcher: CoroutineDispatcher
    ): LocationsDataSource {
        return LocationsLocal(database.locationsDao(), dispatcher)
    }

    @JvmStatic
    @Singleton
    @EventsLocal
    @Provides
    fun provideEventsLocal(
        database: AmaDatabase,
        dispatcher: CoroutineDispatcher
    ): EventsDataSource {
        return EventsLocal(database.eventsDao(), dispatcher)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDatabase(context: Context): AmaDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AmaDatabase::class.java,
            "AMA.db"
        ).build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindConfigurationsRepository(
        repository: ConfigurationsRepository
    ): ConfigurationsDataSource

    @Singleton
    @Binds
    abstract fun bindLocationsRepository(
        repository: LocationsRepository
    ): LocationsDataSource

    @Singleton
    @Binds
    abstract fun bindEventsRepository(
        repository: EventsRepository
    ): EventsDataSource
}