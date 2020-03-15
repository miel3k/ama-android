package com.ama.data.events

import androidx.lifecycle.LiveData
import com.ama.data.RepositoryResult
import com.ama.data.events.model.Event

interface EventsDataSource {

    suspend fun getEvents(eventId: String): LiveData<List<Event>>

    suspend fun saveEvent(event: Event): RepositoryResult<Event>
}