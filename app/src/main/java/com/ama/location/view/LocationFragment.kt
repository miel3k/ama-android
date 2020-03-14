package com.ama.location.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ama.R
import com.ama.core.switchView
import com.ama.location.service.LocationForegroundService
import com.ama.location.service.ServiceAction
import com.ama.location.viewmodel.LocationViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_location.*
import javax.inject.Inject

class LocationFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LocationViewModel> { viewModelFactory }

    private val eventsAdapter by lazy { EventsAdapter() }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder =
                service as LocationForegroundService.LocationForegroundServiceBinder
            setupStartStopButton(binder)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEventsRecyclerView()
        setupChangeConfigurationButton()
        setupClearButton()
        setupEventsObserver()
        setupConfigurationObserver()
    }

    override fun onStart() {
        context?.let {
            val serviceIntent = getLocationForegroundServiceIntent(it)
            it.bindService(
                serviceIntent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
        super.onStart()
    }

    override fun onStop() {
        context?.unbindService(serviceConnection)
        super.onStop()
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

    private fun setupClearButton() {
        btn_clear_logs.setOnClickListener {
            viewModel.saveLocation()
        }
    }

    private fun setupStartStopButton(
        serviceBinder: LocationForegroundService.LocationForegroundServiceBinder
    ) {
        val labelId = if (serviceBinder.isServiceStarted()) {
            R.string.stop
        } else {
            R.string.start
        }
        btn_start_stop.run {
            text = getString(labelId)
            setOnClickListener {
                val serviceAction =
                    getLocationForegroundServiceAction(isEnabled)
                startLocationForegroundServiceIntent(serviceAction)
            }
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

    private fun getLocationForegroundServiceAction(isEnabled: Boolean) =
        if (isEnabled) ServiceAction.START else ServiceAction.STOP

    private fun startLocationForegroundServiceIntent(serviceAction: String) {
        context?.let {
            val serviceIntent = getLocationForegroundServiceIntent(it)
            serviceIntent.setServiceAction(serviceAction)
            ContextCompat.startForegroundService(it, serviceIntent)
        }
    }

    private fun Intent.setServiceAction(serviceAction: String) {
        action = serviceAction
    }

    private fun getLocationForegroundServiceIntent(context: Context) =
        Intent(context, LocationForegroundService::class.java)
}