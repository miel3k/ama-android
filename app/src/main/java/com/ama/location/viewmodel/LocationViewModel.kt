package com.ama.location.viewmodel

import androidx.lifecycle.*
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration
import com.ama.data.events.model.Event
import com.ama.data.locations.LocationsDataSource
import com.ama.data.locations.model.Location
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    private val configurationsRepository: ConfigurationsDataSource,
    private val locationsRepository: LocationsDataSource
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
    val isServiceStarted = MutableLiveData<Boolean>().apply { value = false }
    val events = MutableLiveData<List<Event>>()

    fun changeLocationServiceStatus(isStarted: Boolean) {
        isServiceStarted.value = isStarted
    }

    fun inverseLocationServiceStatus() {
        val currentStatus = isServiceStarted.value
        currentStatus?.let {
            isServiceStarted.value = !it
        }
    }

    fun saveLocation() {
        viewModelScope.launch {
            val location = Location()
            locationsRepository.saveLocation(location)
        }
    }
}