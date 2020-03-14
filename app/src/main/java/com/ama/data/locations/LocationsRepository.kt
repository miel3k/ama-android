package com.ama.data.locations

import com.ama.data.RepositoryResult
import com.ama.data.locations.model.Location
import com.ama.di.application.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    @ApplicationModule.LocationsRemote private val locationsRemote: LocationsDataSource,
    @ApplicationModule.LocationsLocal private val locationsLocal: LocationsDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocationsDataSource {

    override suspend fun loadLocations(): RepositoryResult<List<Location>> {
        val result = locationsRemote.loadLocations()
        if (result is RepositoryResult.Success) {
            locationsLocal.saveLocations(result.data)
        }
        return result
    }

    override suspend fun saveLocations(locations: List<Location>): RepositoryResult<List<Location>> {
        return locationsLocal.saveLocations(locations)
    }

    override suspend fun saveLocation(location: Location): RepositoryResult<Location> {
        val result = locationsRemote.saveLocation(location)
        if (result is RepositoryResult.Success) {
            locationsLocal.saveLocation(result.data)
        }
        return result
    }
}