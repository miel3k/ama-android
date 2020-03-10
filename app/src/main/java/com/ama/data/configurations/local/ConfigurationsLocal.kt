package com.ama.data.configurations.local

import com.ama.data.RepositoryResult
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConfigurationsLocal internal constructor(
    private val configurationsDao: ConfigurationsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ConfigurationsDataSource {

    override suspend fun getConfigurations(): RepositoryResult<List<Configuration>> =
        withContext(dispatcher)
        {
            return@withContext try {
                RepositoryResult.Success(configurationsDao.getConfigurations())
            } catch (e: Exception) {
                RepositoryResult.Error(e)
            }
        }

    override suspend fun getConfiguration(configurationId: String): RepositoryResult<Configuration> =
        withContext(dispatcher)
        {
            return@withContext try {
                val configuration =
                    configurationsDao.getConfiguration(configurationId)
                RepositoryResult.Success(configuration)
            } catch (e: Exception) {
                RepositoryResult.Error(e)
            }
        }
}