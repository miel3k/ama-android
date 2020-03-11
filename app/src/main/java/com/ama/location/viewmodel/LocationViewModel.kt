package com.ama.location.viewmodel

import androidx.lifecycle.*
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration
import com.ama.location.model.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    private val configurationsRepository: ConfigurationsDataSource
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
    val events = MutableLiveData<List<Event>>()
}