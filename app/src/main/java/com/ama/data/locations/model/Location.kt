package com.ama.data.locations.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "Locations")
data class Location @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "locationId")
    @field:Json(name = "locationId")
    var id: String = "",
    @ColumnInfo(name = "accuracy")
    @field:Json(name = "accuracy")
    var accuracy: Float = 0.0f,
    @ColumnInfo(name = "altitude")
    @field:Json(name = "altitude")
    var altitude: Double = 0.0,
    @ColumnInfo(name = "bearing")
    @field:Json(name = "bearing")
    var bearing: Float = 0.0f,
    @ColumnInfo(name = "latitude")
    @field:Json(name = "latitude")
    var latitude: Double = 0.0,
    @ColumnInfo(name = "longitude")
    @field:Json(name = "longitude")
    var longitude: Double = 0.0,
    @ColumnInfo(name = "provider")
    @field:Json(name = "provider")
    var provider: String = "",
    @ColumnInfo(name = "speed")
    @field:Json(name = "speed")
    var speed: Float = 0.0f,
    @ColumnInfo(name = "satellites")
    @field:Json(name = "satellites")
    var satellites: Int = 0,
    @ColumnInfo(name = "time")
    @field:Json(name = "time")
    var time: Long = 0,
    @ColumnInfo(name = "serial")
    @field:Json(name = "serial")
    var serial: String = "",
    @ColumnInfo(name = "deviceId")
    @field:Json(name = "deviceId")
    var deviceId: String = "",
    @ColumnInfo(name = "platform")
    @field:Json(name = "platform")
    var platform: String = "",
    @ColumnInfo(name = "platformVersion")
    @field:Json(name = "platformVersion")
    var platformVersion: Int = 0,
    @ColumnInfo(name = "batteryLevel")
    @field:Json(name = "batteryLevel")
    var batteryLevel: Int = 0
)