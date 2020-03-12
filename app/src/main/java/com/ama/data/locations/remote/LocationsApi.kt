package com.ama.data.locations.remote

import com.ama.data.locations.model.Location
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface LocationsApi {

    @GET("/locations.json")
    fun getLocationsAsync(): Deferred<Response<List<Location>>>

    @POST("/locations.json")
    fun saveLocation(
        @Body body: RequestBody,
        @HeaderMap headers: Map<String, String>
    ): Deferred<Response<Location>>
}