package com.ama.data.configurations.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Configurations")
data class Configuration @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "configurationId")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "token")
    var token: String = "",
    @ColumnInfo(name = "deviceId")
    var deviceId: String = "",
    @ColumnInfo(name = "minutesInterval")
    var minutesInterval: Int
)