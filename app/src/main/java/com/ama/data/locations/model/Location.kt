package com.ama.data.locations.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "Locations")
data class Location @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "locationId")
    @field:Json(name = "locationId")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "acc")
    @field:Json(name = "acc")
    var acc: Int = 0,
    @ColumnInfo(name = "alt")
    @field:Json(name = "alt")
    var alt: Double = 0.0,
    @ColumnInfo(name = "bea")
    @field:Json(name = "bea")
    var bea: Int = 0,
    @ColumnInfo(name = "lat")
    @field:Json(name = "lat")
    var lat: Double = 0.0,
    @ColumnInfo(name = "long")
    @field:Json(name = "long")
    var long: Double = 0.0,
    @ColumnInfo(name = "prov")
    @field:Json(name = "prov")
    var prov: String = "",
    @ColumnInfo(name = "spd")
    @field:Json(name = "spd")
    var spd: Int = 0,
    @ColumnInfo(name = "sat")
    @field:Json(name = "sat")
    var sat: Int = 0,
    @ColumnInfo(name = "time")
    @field:Json(name = "time")
    var time: String = "",
    @ColumnInfo(name = "serial")
    @field:Json(name = "serial")
    var serial: String = "",
    @ColumnInfo(name = "tid")
    @field:Json(name = "tid")
    var tid: String = "",
    @ColumnInfo(name = "plat")
    @field:Json(name = "plat")
    var plat: String = "",
    @ColumnInfo(name = "platVer")
    @field:Json(name = "platVer")
    var platVer: String = "",
    @ColumnInfo(name = "bat")
    @field:Json(name = "bat")
    var bat: Int = 0
)