package com.ama.location.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ama.R
import com.ama.core.switchView
import com.ama.location.viewmodel.LocationViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_location.*
import javax.inject.Inject

class LocationFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LocationViewModel> { viewModelFactory }

    private val eventsAdapter by lazy { EventsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChangeConfigurationButton()
        setupEventsRecyclerView()
        setupEventsObserver()
        setupConfigurationObserver()
    }

    private fun setupEventsRecyclerView() {
        rv_events.apply {
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupChangeConfigurationButton() {
        btn_change_configuration.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupEventsObserver() {
        viewModel.events.observe(viewLifecycleOwner, Observer {
            vs_events.switchView(it.isEmpty(), rv_events, v_empty_state)
            eventsAdapter.updateEvents(it)
        })
    }

    private fun setupConfigurationObserver() {
        viewModel.configuration.observe(viewLifecycleOwner, Observer {
            tv_title.text = it.id
            tv_name.text = it.name
        })
    }
}