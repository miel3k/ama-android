package com.ama.presentation.location.viewmodel

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

    private var configurationId: String? = null
    val configuration: LiveData<Configuration> by lazy {
        MediatorLiveData<Configuration>().apply {
            configurationId?.let {
                viewModelScope.launch {
                    addSource(configurationsRepository.getConfiguration(it)) {
                        value = it
                    }
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
    val isServiceStarted = MutableLiveData<Boolean>()

    fun setupViewModel(newConfigurationId: String) {
        configurationId = newConfigurationId
    }

    fun changeLocationServiceStatus(isStarted: Boolean) {
        val oldIsServiceStarted = isServiceStarted.value
        when {
            isStarted -> isServiceStarted.value = isStarted
            oldIsServiceStarted == true -> isServiceStarted.value = isStarted
        }
    }

    fun inverseLocationServiceStatus() {
        val oldIsServiceStarted = isServiceStarted.value ?: false
        isServiceStarted.value = !oldIsServiceStarted
    }

    fun clearEvents() {
        viewModelScope.launch {
            eventsRepository.deleteEvents()
        }
    }
}