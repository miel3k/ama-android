package com.ama.data.locations.remote

import com.ama.data.locations.model.Location
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface LocationsApi {

    @GET("/locations.json")
    fun getLocationsAsync(): Deferred<Response<List<Location>>>
}