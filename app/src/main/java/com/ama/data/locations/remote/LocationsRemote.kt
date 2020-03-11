package com.ama.data.locations.remote

import com.ama.data.RepositoryResult
import com.ama.data.locations.LocationsDataSource
import com.ama.data.locations.model.Location
import java.io.IOException

class LocationsRemote internal constructor(
    private val locationsApi: LocationsApi
) : LocationsDataSource {

    override suspend fun loadLocations(): RepositoryResult<List<Location>> {
        val response = locationsApi.getLocationsAsync().await()
        return if (response.isSuccessful) {
            RepositoryResult.Success(response.body()!!)
        } else {
            val message = response.errorBody().toString()
            RepositoryResult.Error(IOException(message))
        }
    }

    override suspend fun saveLocation(location: Location) {
        TODO("Not yet implemented")
    }
}