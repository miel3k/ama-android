package com.ama.data.locations.remote

import com.ama.data.RepositoryResult
import com.ama.data.locations.LocationsDataSource
import com.ama.data.locations.model.Location
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
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

    override suspend fun saveLocation(location: Location): RepositoryResult<Location> {
        val headers = mutableMapOf<String, String>()
        val json = JSONObject().apply {
            location.let {
                put("locationId", it.id)
                put("acc", it.acc)
                put("alt", it.alt)
                put("bea", it.bea)
                put("lat", it.lat)
                put("long", it.long)
                put("prov", it.prov)
                put("spd", it.spd)
                put("sat", it.sat)
                put("time", it.time)
                put("serial", it.serial)
                put("tid", it.tid)
                put("plat", it.plat)
                put("platVer", it.platVer)
                put("bat", it.bat)
            }
        }
        val body = RequestBody.create(
            MediaType.parse("application/json"),
            json.toString()
        )
        val response = locationsApi.saveLocation(body, headers).await()
        return if (response.isSuccessful) {
            RepositoryResult.Success(response.body()!!)
        } else {
            val message = response.errorBody().toString()
            RepositoryResult.Error(IOException(message))
        }
    }
}