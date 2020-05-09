package com.ama.data.configurations.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "Configurations")
data class Configuration @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "configurationId")
    @field:Json(name = "configurationId")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name")
    @field:Json(name = "name")
    var name: String = "",
    @ColumnInfo(name = "token")
    @field:Json(name = "token")
    var token: String = "",
    @ColumnInfo(name = "deviceId")
    @field:Json(name = "deviceId")
    var deviceId: String = "",
    @ColumnInfo(name = "minutesInterval")
    @field:Json(name = "minutesInterval")
    var minutesInterval: Int = 0
)