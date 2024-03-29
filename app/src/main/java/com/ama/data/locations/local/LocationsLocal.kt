package com.ama.data.locations.local

import com.ama.data.RepositoryResult
import com.ama.data.locations.LocationsDataSource
import com.ama.data.locations.model.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationsLocal internal constructor(
    private val locationsDao: LocationsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocationsDataSource {

    override suspend fun loadLocations(): RepositoryResult<List<Location>> =
        withContext(dispatcher)
        {
            return@withContext try {
                RepositoryResult.Success(locationsDao.getAll())
            } catch (e: Exception) {
                RepositoryResult.Error(e)
            }
        }

    override suspend fun saveLocations(locations: List<Location>) =
        withContext(dispatcher)
        {
            return@withContext try {
                locationsDao.insertAll(locations)
                RepositoryResult.Success(locations)
            } catch (e: Exception) {
                RepositoryResult.Error(e)
            }
        }

    override suspend fun saveLocation(location: Location): RepositoryResult<Location> =
        withContext(dispatcher)
        {
            return@withContext try {
                locationsDao.insert(location)
                RepositoryResult.Success(location)
            } catch (e: Exception) {
                RepositoryResult.Error(e)
            }
        }
}