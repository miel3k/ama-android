package com.ama.data.configurations.local

import androidx.room.Dao
import androidx.room.Query
import com.ama.data.configurations.model.Configuration

@Dao
interface ConfigurationsDao {

    @Query("SELECT * FROM Configurations")
    suspend fun getConfigurations(): List<Configuration>
}