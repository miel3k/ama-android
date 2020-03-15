package com.ama.data.events

import androidx.lifecycle.LiveData
import com.ama.data.RepositoryResult
import com.ama.data.events.model.Event
import com.ama.di.application.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class EventsRepository @Inject constructor(
    @ApplicationModule.EventsLocal private val eventsLocal: EventsDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : EventsDataSource {

    override suspend fun getEvents(): LiveData<List<Event>> {
        return eventsLocal.getEvents()
    }

    override suspend fun saveEvent(event: Event): RepositoryResult<Event> {
        return eventsLocal.saveEvent(event)
    }

    override suspend fun deleteEvents() {
        eventsLocal.deleteEvents()
    }
}