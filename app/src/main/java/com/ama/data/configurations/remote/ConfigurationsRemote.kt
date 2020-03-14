package com.ama.data.configurations.remote

import com.ama.data.RepositoryResult
import com.ama.data.call
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration

class ConfigurationsRemote internal constructor(
    private val configurationsApi: ConfigurationsApi
) : ConfigurationsDataSource {

    override suspend fun loadConfiguration(configurationId: String): RepositoryResult<Configuration> {
        return configurationsApi.getConfigurationAsync(configurationId).call()
    }
}