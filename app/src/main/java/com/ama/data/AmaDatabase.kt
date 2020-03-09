package com.ama.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ama.data.configurations.local.ConfigurationsDao
import com.ama.data.configurations.model.Configuration

@Database(entities = [Configuration::class], version = 1, exportSchema = false)
abstract class AmaDatabase : RoomDatabase() {

    abstract fun configurationsDao(): ConfigurationsDao
}