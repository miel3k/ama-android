package com.ama.location.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.ama.location.LocationTransitions
import com.ama.location.LocationTransitions.Companion.TRANSITION_ACTION_RECEIVER
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
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

    private val locationTransitions by lazy {
        LocationTransitions(applicationContext)
    }

    private val transitionReceiver by lazy {
        TransitionReceiver()
    }

    private val locationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val batteryManager by lazy {
        applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    }

    private val locationListener by lazy {
        object : LocationListener {
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
    }

    private val binder = LocationForegroundServiceBinder()
    private lateinit var deviceId: String
    private var isStarted = false
    private var isStill = false

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
        deviceId = intent.getStringExtra(DEVICE_ID).orEmpty()
        val interval = intent.getIntExtra(
            LOCATION_UPDATES_INTERVAL,
            LOCATION_UPDATES_INTERVAL_DEFAULT_VALUE
        )
        if (intent.action == ServiceAction.START) start(interval) else stop()
        return START_STICKY
    }

    private fun start(interval: Int) {
        if (isStarted) return
        val locationNotification = LocationNotification(applicationContext).create()
        startForeground(NotificationId.LOCATION_FOREGROUND_SERVICE, locationNotification)
        startLocationTracking(interval)
        registerTransitionReceiver()
        startTransitionsTracking()
        isStarted = true
    }

    private fun stop() {
        if (isStarted) {
            locationManager.removeUpdates(locationListener)
            stopTransitionsTracking()
            unregisterTransitionReceiver()
            stopForeground(true)
            stopSelf()
        }
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

    private fun startTransitionsTracking() {
        locationTransitions.startTrackingTask()
    }

    private fun stopTransitionsTracking() {
        locationTransitions.stopTrackingTask()
    }

    private fun registerTransitionReceiver() {
        applicationContext.registerReceiver(
            transitionReceiver,
            IntentFilter(TRANSITION_ACTION_RECEIVER)
        )
    }

    private fun unregisterTransitionReceiver() {
        applicationContext.unregisterReceiver(transitionReceiver)
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
            applicationContext,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
        if (isPermissionGranted) {
            if (!isStill) {
                eventsRepository.saveEvent(createLocationEvent())
                locationsRepository.saveLocation(location.toLocation())
            }
        } else {
            stop()
        }
    }

    private fun createLocationEvent() = Event(
        id = UUID.randomUUID().toString(),
        date = DateTime.now().toString(),
        message = "Location sent successfully"
    )

    private fun createTransitionEvent(type: String, activity: String) = Event(
        id = UUID.randomUUID().toString(),
        date = DateTime.now().toString(),
        message = "Transition registered: $type -> $activity"
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
            deviceId = deviceId,
            platform = PLATFORM_ANDROID,
            platformVersion = Build.VERSION.SDK_INT,
            batteryLevel = batteryManager.getBatteryLevel()
        )
    }

    private fun Location.getSatellites() = extras.getInt(SATELLITES)

    private fun BatteryManager.getBatteryLevel() =
        getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    @SuppressLint("HardwareIds")
    private fun getAndroidId(context: Context) = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    inner class LocationForegroundServiceBinder : Binder() {
        fun isServiceStarted() = this@LocationForegroundService.isStarted
    }

    inner class TransitionReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ActivityTransitionResult.hasResult(intent) && isStarted) {
                ActivityTransitionResult.extractResult(intent)?.transitionEvents?.forEach {
                    val activity = toActivityString(it.activityType)
                    val type = toTransitionType(it.transitionType)
                    val transitionEvent = createTransitionEvent(type, activity)
                    isStill = it.isStill()
                    GlobalScope.launch { eventsRepository.saveEvent(transitionEvent) }
                }
            }
        }

        private fun toActivityString(activity: Int) = when (activity) {
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.WALKING -> "WALKING"
            else -> "UNKNOWN"
        }

        private fun toTransitionType(transitionType: Int) = when (transitionType) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
            else -> "UNKNOWN"
        }

        private fun ActivityTransitionEvent.isStill() = activityType == DetectedActivity.STILL
                && transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER
    }

    companion object {
        private const val SATELLITES = "satellites"
        private const val LOCATION_MIN_DISTANCE = 0f
        private const val PLATFORM_ANDROID = "Android"
        const val LOCATION_UPDATES_INTERVAL_DEFAULT_VALUE = 15
        const val LOCATION_UPDATES_INTERVAL = "LOCATION_UPDATES_INTERVAL"
        const val DEVICE_ID = "DEVICE_ID"
    }
}