package com.ama.data.events.local

import androidx.lifecycle.LiveData
import com.ama.data.RepositoryResult
import com.ama.data.events.EventsDataSource
import com.ama.data.events.model.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventsLocal internal constructor(
    private val eventsDao: EventsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : EventsDataSource {

    override suspend fun getEvents(): LiveData<List<Event>> =
        withContext(dispatcher) {
            return@withContext eventsDao.getAll()
        }

    override suspend fun saveEvent(event: Event): RepositoryResult<Event> =
        withContext(dispatcher)
        {
            return@withContext try {
                eventsDao.insert(event)
                RepositoryResult.Success(event)
            } catch (e: Exception) {
                RepositoryResult.Error(e)
            }
        }

    override suspend fun deleteEvents() =
        withContext(dispatcher)
        {
            return@withContext eventsDao.deleteAll()
        }
}