package com.ama.location.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.ama.NotificationId
import com.ama.core.getLocationPermissions
import com.ama.data.events.EventsDataSource
import com.ama.data.events.model.Event
import com.ama.data.locations.LocationsDataSource
import com.ama.location.LocationNotification
import dagger.android.AndroidInjection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

class LocationForegroundService : Service() {

    @Inject
    lateinit var eventsRepository: EventsDataSource

    @Inject
    lateinit var locationsRepository: LocationsDataSource

    private val locationNotification by lazy {
        LocationNotification(applicationContext).create()
    }
    private val binder = LocationForegroundServiceBinder()
    private var isStarted = false
    private val locationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            GlobalScope.launch {
                val testEvent = Event(
                    date = DateTime.now().toString(),
                    message = location.latitude.toString()
                )
                eventsRepository.saveEvent(testEvent)
                val testLocation = com.ama.data.locations.model.Location()
                locationsRepository.saveLocation(testLocation)
            }
        }

        override fun onStatusChanged(
            provider: String,
            status: Int,
            extras: Bundle
        ) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        if (intent?.action == ServiceAction.START) start() else stop()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun start() {
        if (isStarted) return
        isStarted = true
        startForeground(
            NotificationId.LOCATION_FOREGROUND_SERVICE,
            locationNotification
        )
        startLocationTracking()
    }

    private fun startLocationTracking() {
        val permissions = getLocationPermissions()
        val arePermissionsGranted = permissions.all {
            ActivityCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (arePermissionsGranted) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                15000L,
                0f,
                locationListener
            )
        } else {
            stop()
        }
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
        isStarted = false
    }

    inner class LocationForegroundServiceBinder : Binder() {

        fun isServiceStarted() = this@LocationForegroundService.isStarted
    }
}