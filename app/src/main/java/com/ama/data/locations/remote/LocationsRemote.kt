package com.ama.data.locations.remote

import com.ama.data.MediaTypes
import com.ama.data.RepositoryResult
import com.ama.data.call
import com.ama.data.locations.LocationsDataSource
import com.ama.data.locations.model.Location
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.RequestBody

class LocationsRemote internal constructor(
    private val locationsApi: LocationsApi
) : LocationsDataSource {

    override suspend fun loadLocations(): RepositoryResult<List<Location>> {
        return locationsApi.getLocationsAsync().call()
    }

    override suspend fun saveLocation(location: Location): RepositoryResult<Location> {
        val headers = mutableMapOf<String, String>()
        val adapter = Moshi.Builder().build().adapter(Location::class.java)
        val json = adapter.toJson(location)
        val body = RequestBody.create(
            MediaType.parse(MediaTypes.APPLICATION_JSON),
            json
        )
        return locationsApi.saveLocationAsync(body, headers).call()
    }
}