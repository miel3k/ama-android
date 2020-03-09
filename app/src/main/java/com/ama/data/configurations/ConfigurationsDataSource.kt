package com.ama.data.configurations

import com.ama.data.RepositoryResult
import com.ama.data.configurations.model.Configuration

interface ConfigurationsDataSource {

    suspend fun getConfigurations(): RepositoryResult<List<Configuration>>
}