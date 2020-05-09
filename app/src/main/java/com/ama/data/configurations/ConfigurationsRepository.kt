package com.ama.data.configurations

import com.ama.data.RepositoryResult
import com.ama.data.configurations.model.Configuration
import com.ama.di.application.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConfigurationsRepository @Inject constructor(
    @ApplicationModule.ConfigurationsRemote private val configurationsRemote: ConfigurationsDataSource,
    @ApplicationModule.ConfigurationsLocal private val configurationsLocal: ConfigurationsDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ConfigurationsDataSource {

    override suspend fun getConfiguration(configurationId: String) = withContext(dispatcher) {
        configurationsLocal.getConfiguration(configurationId)
    }

    override suspend fun loadConfiguration(configurationId: String): RepositoryResult<Configuration> {
        val configurationResult = configurationsRemote.loadConfiguration(configurationId)
        if (configurationResult is RepositoryResult.Success) {
            saveConfiguration(configurationResult.data)
        }
        return configurationResult
    }

    override suspend fun saveConfiguration(configuration: Configuration) {
        configurationsLocal.saveConfiguration(configuration)
    }
}