package com.ama.location.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.provider.Settings
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
import java.util.*
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
    private lateinit var trackedDeviceId: String
    private var isStarted = false
    private val locationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val batteryManager by lazy {
        applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    }
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            GlobalScope.launch {
                sendLocation(location)
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
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {
        trackedDeviceId = intent.getStringExtra(TRACKED_DEVICE_ID).orEmpty()
        val interval = intent.getIntExtra(
            LOCATION_UPDATES_INTERVAL,
            LOCATION_UPDATES_INTERVAL_DEFAULT_VALUE
        )
        if (intent.action == ServiceAction.START) start(interval) else stop()
        return START_STICKY
    }

    private fun start(interval: Int) {
        if (isStarted) return
        isStarted = true
        startForeground(NotificationId.LOCATION_FOREGROUND_SERVICE, locationNotification)
        startLocationTracking(interval)
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
        isStarted = false
    }

    private fun startLocationTracking(interval: Int) {
        val permissions = getLocationPermissions()
        val arePermissionsGranted = permissions.all {
            ActivityCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (arePermissionsGranted) {
            locationManager.requestLocationUpdates(
                getAvailableLocationProvider(),
                (interval * 1000).toLong(),
                LOCATION_MIN_DISTANCE,
                locationListener
            )
        } else {
            stop()
        }
    }

    private fun getAvailableLocationProvider(): String {
        return if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationManager.GPS_PROVIDER
        } else {
            LocationManager.NETWORK_PROVIDER
        }
    }

    private suspend fun sendLocation(location: Location) {
        val isPermissionGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
        if (isPermissionGranted) {
            eventsRepository.saveEvent(createLocationEvent())
            locationsRepository.saveLocation(location.toLocation())
        } else {
            stop()
        }
    }

    private fun createLocationEvent() = Event(
        id = UUID.randomUUID().toString(),
        date = DateTime.now().toString(),
        message = "Location sent successfully"
    )

    private fun Location.toLocation() = let {
        com.ama.data.locations.model.Location(
            id = UUID.randomUUID().toString(),
            accuracy = it.accuracy,
            altitude = it.altitude,
            bearing = it.bearing,
            latitude = it.latitude,
            longitude = it.longitude,
            provider = it.provider,
            speed = it.speed,
            satellites = it.getSatellites(),
            time = it.time,
            serial = getAndroidId(applicationContext),
            trackedDeviceId = trackedDeviceId,
            platform = PLATFORM_ANDROID,
            platformVersion = Build.VERSION.SDK_INT,
            batteryLevel = getBatteryLevel()
        )
    }

    private fun Location.getSatellites() = extras.getInt(SATELLITES)

    private fun getBatteryLevel() =
        batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    @SuppressLint("HardwareIds")
    private fun getAndroidId(context: Context) = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    inner class LocationForegroundServiceBinder : Binder() {
        fun isServiceStarted() = this@LocationForegroundService.isStarted
    }

    companion object {
        private const val SATELLITES = "satellites"
        private const val LOCATION_MIN_DISTANCE = 0f
        private const val PLATFORM_ANDROID = "Android"
        const val LOCATION_UPDATES_INTERVAL_DEFAULT_VALUE = 15
        const val LOCATION_UPDATES_INTERVAL = "LOCATION_UPDATES_INTERVAL"
        const val TRACKED_DEVICE_ID = "TRACKED_DEVICE_ID"
    }
}