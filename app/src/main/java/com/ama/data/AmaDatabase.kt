package com.ama.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ama.data.configurations.local.ConfigurationsDao
import com.ama.data.configurations.model.Configuration
import com.ama.data.locations.local.LocationsDao
import com.ama.data.locations.model.Location

@Database(
    entities = [Configuration::class, Location::class],
    version = 1,
    exportSchema = false
)
abstract class AmaDatabase : RoomDatabase() {

    abstract fun configurationsDao(): ConfigurationsDao
    abstract fun locationsDao(): LocationsDao
}