package com.ama.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ama.data.RepositoryResult
import com.ama.data.configurations.ConfigurationsDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfigurationViewModel @Inject constructor(
    private val configurationsRepository: ConfigurationsDataSource
) : ViewModel() {

    fun loadConfigurations() {
        viewModelScope.launch {
            when (val result =
                configurationsRepository.getConfiguration("123456")) {
                is RepositoryResult.Success -> {
                    val list = result.data
                    list
                }
                is RepositoryResult.Error -> {

                }
            }
        }
    }
}