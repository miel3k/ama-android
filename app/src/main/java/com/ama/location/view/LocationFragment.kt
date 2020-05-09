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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ama.AmaApplication
import com.ama.R
import com.ama.core.switchView
import com.ama.location.service.LocationForegroundService
import com.ama.location.service.ServiceAction
import com.ama.location.viewmodel.LocationViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_location.*
import javax.inject.Inject

class LocationFragment : DaggerFragment(), LocationOnBack {

    private val args by navArgs<LocationFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LocationViewModel> { viewModelFactory }

    private val eventsAdapter by lazy { EventsAdapter() }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationForegroundService.LocationForegroundServiceBinder
            val isServiceStarted = binder.isServiceStarted()
            viewModel.changeLocationServiceStatus(isServiceStarted)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_location, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setupViewModel(args.configurationId)
        setupEventsRecyclerView()
        setupChangeConfigurationButton()
        setupClearButton()
        setupStartStopButton()
        setupEventsObserver()
        setupConfigurationObserver()
        setupIsServiceStartedObserver()
    }

    override fun onStart() {
        context?.let {
            val serviceIntent = getLocationForegroundServiceIntent(it)
            it.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
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
            viewModel.changeLocationServiceStatus(false)
            context?.let { context -> removeConfigurationId(context) }
            findNavController().popBackStack()
        }
    }

    private fun setupClearButton() {
        btn_clear_logs.setOnClickListener {
            viewModel.clearEvents()
        }
    }

    private fun setupStartStopButton() {
        btn_start_stop.setOnClickListener {
            viewModel.inverseLocationServiceStatus()
        }
    }

    private fun getStartStopButtonLabelId(isStarted: Boolean) =
        if (isStarted) R.string.stop else R.string.start

    private fun setupEventsObserver() {
        viewModel.events.observe(viewLifecycleOwner) {
            vs_events.switchView(it.isEmpty(), rv_events, v_empty_state)
            eventsAdapter.updateEvents(it)
        }
    }

    private fun setupConfigurationObserver() {
        viewModel.configuration.observe(viewLifecycleOwner) {
            tv_title.text = it.id
            tv_name.text = it.name
        }
    }

    private fun setupIsServiceStartedObserver() {
        viewModel.isServiceStarted.observe(viewLifecycleOwner) {
            val interval = viewModel.configuration.value?.minutesInterval
                ?: LocationForegroundService.LOCATION_UPDATES_INTERVAL_DEFAULT_VALUE
            val deviceId = viewModel.configuration.value?.deviceId.orEmpty()
            val serviceAction = getLocationForegroundServiceAction(!it)
            startLocationForegroundServiceIntent(serviceAction, deviceId, interval)
            btn_start_stop.text = getString(getStartStopButtonLabelId(it))
        }
    }

    private fun getLocationForegroundServiceAction(isEnabled: Boolean) =
        if (isEnabled) ServiceAction.STOP else ServiceAction.START

    private fun startLocationForegroundServiceIntent(
        serviceAction: String,
        deviceId: String,
        interval: Int
    ) {
        context?.let {
            val serviceIntent = getLocationForegroundServiceIntent(it, deviceId, interval)
            serviceIntent.setServiceAction(serviceAction)
            ContextCompat.startForegroundService(it, serviceIntent)
        }
    }

    private fun Intent.setServiceAction(serviceAction: String) {
        action = serviceAction
    }

    private fun getLocationForegroundServiceIntent(
        context: Context,
        deviceId: String = "",
        interval: Int = LocationForegroundService.LOCATION_UPDATES_INTERVAL_DEFAULT_VALUE
    ) = Intent(context, LocationForegroundService::class.java).apply {
        putExtra(LocationForegroundService.DEVICE_ID, deviceId)
        putExtra(LocationForegroundService.LOCATION_UPDATES_INTERVAL, interval)
    }

    override fun onBackPressed() {
        viewModel.changeLocationServiceStatus(false)
        context?.let { removeConfigurationId(it) }
    }

    private fun removeConfigurationId(context: Context) {
        context.getSharedPreferences(AmaApplication.AMA_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .remove(AmaApplication.CONFIGURATION_ID)
            .apply()
    }
}