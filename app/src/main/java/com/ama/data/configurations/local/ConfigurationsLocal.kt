package com.ama.data.configurations.local

import androidx.lifecycle.LiveData
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConfigurationsLocal internal constructor(
    private val configurationsDao: ConfigurationsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ConfigurationsDataSource {

    override suspend fun getConfiguration(configurationId: String): LiveData<Configuration> =
        withContext(dispatcher)
        {
            return@withContext configurationsDao.getConfiguration(
                configurationId
            )
        }

    override suspend fun saveConfiguration(configuration: Configuration) =
        withContext(dispatcher)
        {
            configurationsDao.insertConfiguration(configuration)
        }
}