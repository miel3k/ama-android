package com.ama.data.events.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Events")
data class Event @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "eventId")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "message")
    var message: String = ""
)