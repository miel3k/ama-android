package com.ama.data

import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> Deferred<Response<T>>.call(): RepositoryResult<T> {
    val response = await()
    return if (response.isSuccessful) {
        RepositoryResult.Success(response.body()!!)
    } else {
        RepositoryResult.Error(IOException(response.message()))
    }
}