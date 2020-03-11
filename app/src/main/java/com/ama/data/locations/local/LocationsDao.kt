package com.ama.data.locations.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ama.data.locations.model.Location

@Dao
interface LocationsDao {

    @Query("SELECT * FROM Locations")
    suspend fun getLocations(): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: Location)
}