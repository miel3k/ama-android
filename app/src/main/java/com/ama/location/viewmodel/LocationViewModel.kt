package com.ama.location.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ama.location.model.Event
import javax.inject.Inject

class LocationViewModel @Inject constructor() : ViewModel() {

    val events = MutableLiveData<List<Event>>()
}