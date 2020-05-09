package com.ama.presentation.location

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity

class LocationTransitions(private val applicationContext: Context) {

    private val intent by lazy {
        initPendingIntent()
    }

    fun startTrackingTask() {
        val transitions = getTransitions()
        val request = ActivityTransitionRequest(transitions)
        ActivityRecognition.getClient(applicationContext)
            .requestActivityTransitionUpdates(request, intent)
            .addOnSuccessListener {
                Log.d(TAG, "Transitions Api was successfully registered.")
            }
            .addOnFailureListener {
                Log.e(TAG, "Transitions Api was not created!", it)
            }
    }

    fun stopTrackingTask() {
        ActivityRecognition.getClient(applicationContext)
            .removeActivityTransitionUpdates(intent)
            .addOnSuccessListener {
                Log.d(TAG, "Transitions Api was unregistered.")
            }
            .addOnFailureListener {
                Log.e(TAG, "Transitions Api failed to unregister!", it)
            }
    }

    private fun getTransitions() = listOf(
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.ON_FOOT)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.ON_FOOT)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.STILL)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.STILL)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
    )

    private fun initPendingIntent() = PendingIntent.getBroadcast(
        applicationContext,
        0,
        Intent(TRANSITION_ACTION_RECEIVER),
        FLAG_UPDATE_CURRENT
    )

    companion object {
        const val TAG = "LocationTransitions"
        const val TRANSITION_ACTION_RECEIVER = "TRANSITION_ACTION_RECEIVER";
    }
}