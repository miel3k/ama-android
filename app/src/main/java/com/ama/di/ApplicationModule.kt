package com.ama.di

import android.content.Context
import androidx.room.Room
import com.ama.data.AmaDatabase
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.ConfigurationsRepository
import com.ama.data.configurations.local.ConfigurationsLocal
import com.ama.data.configurations.remote.ConfigurationsRemote
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
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
    fun provideConfigurationsRemote(): ConfigurationsDataSource {
        return ConfigurationsRemote
    }

    @JvmStatic
    @Singleton
    @ConfigurationsLocal
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
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindConfigurationRepository(
        repository: ConfigurationsRepository
    ): ConfigurationsDataSource
}