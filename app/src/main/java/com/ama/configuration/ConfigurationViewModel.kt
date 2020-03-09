package com.ama.configuration

import androidx.lifecycle.ViewModel
import com.ama.data.configurations.ConfigurationsDataSource
import javax.inject.Inject

class ConfigurationViewModel @Inject constructor(
    private val configurationsRepository: ConfigurationsDataSource
) : ViewModel() {

}