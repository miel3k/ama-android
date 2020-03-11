package com.ama.data.configurations

import androidx.lifecycle.LiveData
import com.ama.data.RepositoryResult
import com.ama.data.configurations.model.Configuration
import com.ama.di.application.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ConfigurationsRepository @Inject constructor(
    @ApplicationModule.ConfigurationsRemote private val configurationsRemote: ConfigurationsDataSource,
    @ApplicationModule.ConfigurationsLocal private val configurationsLocal: ConfigurationsDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ConfigurationsDataSource {

    override suspend fun getConfiguration(configurationId: String): LiveData<Configuration> {
        return configurationsLocal.getConfiguration(configurationId)
    }

    override suspend fun loadConfiguration(configurationId: String): RepositoryResult<Configuration> {
        val configurationResult =
            configurationsRemote.loadConfiguration(configurationId)
        if (configurationResult is RepositoryResult.Success) {
            configurationsLocal.saveConfiguration(configurationResult.data)
        }
        return configurationResult
    }
}