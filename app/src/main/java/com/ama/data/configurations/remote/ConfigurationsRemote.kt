package com.ama.data.configurations.remote

import com.ama.data.RepositoryResult
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration
import java.io.IOException

class ConfigurationsRemote internal constructor(
    private val configurationsApi: ConfigurationsApi
) : ConfigurationsDataSource {

    override suspend fun getConfigurations(): RepositoryResult<List<Configuration>> {
        val response = configurationsApi.getConfigurationsAsync().await()
        return if (response.isSuccessful) {
            RepositoryResult.Success(response.body()!!)
        } else {
            val message = response.errorBody().toString()
            RepositoryResult.Error(IOException(message))
        }
    }

    override suspend fun loadConfiguration(configurationId: String): RepositoryResult<Configuration> {
        val response =
            configurationsApi.getConfigurationAsync(configurationId).await()
        return if (response.isSuccessful) {
            RepositoryResult.Success(response.body()!!)
        } else {
            val message = response.errorBody().toString()
            RepositoryResult.Error(IOException(message))
        }
    }
}