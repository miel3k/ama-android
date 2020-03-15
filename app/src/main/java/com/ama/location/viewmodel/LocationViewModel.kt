package com.ama.location.viewmodel

import androidx.lifecycle.*
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration
import com.ama.data.events.EventsDataSource
import com.ama.data.events.model.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    private val configurationsRepository: ConfigurationsDataSource,
    private val eventsRepository: EventsDataSource
) : ViewModel() {

    val configuration: LiveData<Configuration> by lazy {
        MediatorLiveData<Configuration>().apply {
            viewModelScope.launch {
                addSource(configurationsRepository.getConfiguration("111222")) {
                    value = it
                }
            }
        }
    }
    val events: LiveData<List<Event>> by lazy {
        MediatorLiveData<List<Event>>().apply {
            viewModelScope.launch {
                addSource(eventsRepository.getEvents()) {
                    value = it
                }
            }
        }
    }
    val isServiceStarted = MutableLiveData<Boolean>().apply { value = false }

    fun changeLocationServiceStatus(isStarted: Boolean) {
        isServiceStarted.value = isStarted
    }

    fun inverseLocationServiceStatus() {
        val currentStatus = isServiceStarted.value
        currentStatus?.let {
            isServiceStarted.value = !it
        }
    }

    fun clearEvents() {
        viewModelScope.launch {
            eventsRepository.deleteEvents()
        }
    }
}