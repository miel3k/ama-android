package com.ama.di.application

import android.content.Context
import androidx.room.Room
import com.ama.data.AmaDatabase
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.ConfigurationsRepository
import com.ama.data.configurations.local.ConfigurationsLocal
import com.ama.data.configurations.remote.ConfigurationsApi
import com.ama.data.configurations.remote.ConfigurationsRemote
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
    abstract fun bindConfigurationRepository(
        repository: ConfigurationsRepository
    ): ConfigurationsDataSource
}