package com.ama.data.configurations

import com.ama.data.RepositoryResult
import com.ama.data.configurations.model.Configuration
import com.ama.di.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ConfigurationsRepository @Inject constructor(
    @ApplicationModule.ConfigurationsRemote private val configurationsRemote: ConfigurationsDataSource,
    @ApplicationModule.ConfigurationsLocal private val configurationsLocal: ConfigurationsDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ConfigurationsDataSource {

    override suspend fun getConfigurations(): RepositoryResult<List<Configuration>> {
        TODO("Not yet implemented")
    }
}