package com.ama.presentation.configuration.viewmodel

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
            when (val result = configurationsRepository.loadConfiguration(configurationId)) {
                is RepositoryResult.Success -> {
                    saveGetConfigurationEvent(true, configurationId)
                    success.postValue(result.data.id)
                    isLoading.value = false
                }
                is RepositoryResult.Error -> {
                    saveGetConfigurationEvent(false, configurationId)
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

    private fun saveGetConfigurationEvent(isSuccess: Boolean, id: String) {
        viewModelScope.launch {
            eventsRepository.saveEvent(createGetConfigurationEvent(isSuccess, id))
        }
    }

    private fun createPermissionEvent(isGranted: Boolean) = Event(
        id = UUID.randomUUID().toString(),
        date = DateTime.now().toString(),
        message = if (isGranted) {
            "Permissions checked:\nstatus = granted"
        } else {
            "Permissions checked:\nstatus = not granted"
        }
    )

    private fun createGetConfigurationEvent(isSuccess: Boolean, id: String) = Event(
        id = UUID.randomUUID().toString(),
        date = DateTime.now().toString(),
        message = if (isSuccess) {
            "GET configuration:\nstatus = success\nid = $id "
        } else {
            "GET configuration:\nstatus = failure\nid = $id"
        }
    )

    sealed class Error {
        object ConfigurationIdEmpty : Error()
        class Remote(val message: String) : Error()
    }
}