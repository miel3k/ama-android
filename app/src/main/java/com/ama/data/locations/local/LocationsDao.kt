package com.ama.data.locations.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ama.data.locations.model.Location

@Dao
interface LocationsDao {

    @Query("SELECT * FROM Locations")
    suspend fun getAll(): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: Location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: List<Location>)
}