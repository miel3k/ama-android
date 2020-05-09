package com.ama.configuration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ama.core.SingleLiveEvent
import com.ama.data.RepositoryResult
import com.ama.data.configurations.ConfigurationsDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfigurationViewModel @Inject constructor(
    private val configurationsRepository: ConfigurationsDataSource
) : ViewModel() {

    val error = SingleLiveEvent<Error>()
    val success = SingleLiveEvent<String>()
    val isLoading = MutableLiveData<Boolean>(false)

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

    sealed class Error {
        object ConfigurationIdEmpty : Error()
        class Remote(val message: String) : Error()
    }
}