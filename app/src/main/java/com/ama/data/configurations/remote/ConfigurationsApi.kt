package com.ama.data.configurations.remote

import com.ama.data.configurations.model.Configuration
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ConfigurationsApi {
    @GET("/configurations.json")
    fun getConfigurationsAsync(): Deferred<Response<List<Configuration>>>

    @GET("/configurations/{id}.json")
    fun getConfigurationAsync(@Path(value = "id") configurationId: String): Deferred<Response<Configuration>>
}