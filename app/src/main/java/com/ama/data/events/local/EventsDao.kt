package com.ama.data.events.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ama.data.events.model.Event

@Dao
interface EventsDao {

    @Query("SELECT * FROM Events")
    fun getAll(): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event)

    @Query("DELETE FROM Events")
    fun deleteAll()
}