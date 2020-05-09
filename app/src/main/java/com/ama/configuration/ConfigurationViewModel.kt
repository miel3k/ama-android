package com.ama.configuration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ama.core.SingleLiveEvent
import com.ama.data.RepositoryResult
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.events.EventsDataSource
import com.ama.data.events.model.Event
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject

class ConfigurationViewModel @Inject constructor(
    private val configurationsRepository: ConfigurationsDataSource,
    private val eventsRepository: EventsDataSource
) : ViewModel() {

    val error = SingleLiveEvent<Error>()
    val success = SingleLiveEvent<String>()
    val isLoading = MutableLiveData(false)

    fun loadConfiguration(configurationId: String) {
        if (configurationId.isEmpty()) {
            error.postValue(Error.ConfigurationIdEmpty)
            return
        }
        isLoading.value = true
        viewModelScope.launch {
            when (val result =
                configurationsRepository.loadConfiguration(configurationId)) {
                is RepositoryResult.Success -> {
                    success.postValue(result.data.id)
                    isLoading.value = false
                }
                is RepositoryResult.Error -> {
                    error.postValue(Error.Remote(result.exception.message.orEmpty()))
                    isLoading.value = false
                }
            }
        }
    }

    fun savePermissionEvent(isGranted: Boolean) {
        viewModelScope.launch {
            eventsRepository.saveEvent(createPermissionEvent(isGranted))
        }
    }

    private fun createPermissionEvent(isGranted: Boolean) = Event(
        id = UUID.randomUUID().toString(),
        date = DateTime.now().toString(),
        message = if (isGranted) "Permissions granted" else "Permissions not granted"
    )

    sealed class Error {
        object ConfigurationIdEmpty : Error()
        class Remote(val message: String) : Error()
    }
}