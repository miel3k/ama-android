package com.ama.data

sealed class RepositoryResult<out R> {

    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val exception: Exception) : RepositoryResult<Nothing>()
    object Loading : RepositoryResult<Nothing>()
}