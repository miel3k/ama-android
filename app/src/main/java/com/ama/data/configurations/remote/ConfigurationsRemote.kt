package com.ama.data.configurations.remote

import com.ama.data.RepositoryResult
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration

object ConfigurationsRemote : ConfigurationsDataSource {

    override suspend fun getConfigurations(): RepositoryResult<List<Configuration>> {
        TODO("Not yet implemented")
    }
}