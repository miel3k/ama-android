package com.ama.data.locations

import com.ama.data.RepositoryResult
import com.ama.data.locations.model.Location

interface LocationsDataSource {

    suspend fun loadLocations(): RepositoryResult<List<Location>>

    suspend fun saveLocations(locations: List<Location>): RepositoryResult<List<Location>> {
        throw NotImplementedError(this::class.java.name)
    }

    suspend fun saveLocation(location: Location): RepositoryResult<Location>
}