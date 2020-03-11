package com.ama.data.configurations

import androidx.lifecycle.LiveData
import com.ama.data.RepositoryResult
import com.ama.data.configurations.model.Configuration

interface ConfigurationsDataSource {

    suspend fun getConfigurations(): RepositoryResult<List<Configuration>>

    suspend fun getConfiguration(configurationId: String): LiveData<Configuration> {
        throw NotImplementedError(this::class.java.name)
    }

    suspend fun loadConfiguration(configurationId: String): RepositoryResult<Configuration> {
        throw NotImplementedError(this::class.java.name)
    }

    suspend fun saveConfiguration(configuration: Configuration) {
        throw NotImplementedError(this::class.java.name)
    }
}