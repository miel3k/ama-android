package com.ama.data.configurations.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ama.data.configurations.model.Configuration

@Dao
interface ConfigurationsDao {

    @Query("SELECT * FROM Configurations")
    suspend fun getConfigurations(): List<Configuration>

    @Query("SELECT * FROM Configurations WHERE configurationId=:id")
    fun getConfiguration(id: String): LiveData<Configuration>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConfiguration(configuration: Configuration)
}