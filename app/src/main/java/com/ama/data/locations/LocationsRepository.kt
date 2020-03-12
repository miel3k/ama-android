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
        return locationsRemote.loadLocations()
    }

    override suspend fun saveLocation(location: Location): RepositoryResult<Location> {
        return locationsRemote.saveLocation(location)
    }
}