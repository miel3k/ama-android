package com.ama.data

import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> Deferred<Response<T>>.call(): RepositoryResult<T> {
    val response = await()
    val responseBody = response.body()
    return when {
        response.isSuccessful && responseBody != null -> {
            RepositoryResult.Success(responseBody)
        }
        response.isSuccessful && responseBody == null -> {
            RepositoryResult.Error(IOException("Configuration not found"))
        }
        else -> {
            RepositoryResult.Error(IOException(response.errorBody().toString()))
        }
    }
}