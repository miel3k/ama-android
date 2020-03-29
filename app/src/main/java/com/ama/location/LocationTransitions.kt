package com.ama.location

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

    private val transitions by lazy {
        initTransitionsList()
    }

    private val request by lazy {
        ActivityTransitionRequest(transitions)
    }

    fun startTrackingTask() {
        val task = ActivityRecognition.getClient(applicationContext)
            .requestActivityTransitionUpdates(request, intent)
        task.addOnSuccessListener {
            Log.d(TAG, "Transitions Api was successfully registered.")
        }
        task.addOnFailureListener { ex ->
            Log.e(TAG, "Transitions Api was not created!", ex)
        }
    }

    fun stopTrackingTask() {
        val task = ActivityRecognition.getClient(applicationContext)
            .removeActivityTransitionUpdates(intent)
        task.addOnSuccessListener {
            Log.d(TAG, "Transitions Api was unregistered.")
        }
        task.addOnFailureListener { ex ->
            Log.e(TAG, "Transitions Api failed to unregister!", ex)
        }

    }

    private fun initTransitionsList(): List<ActivityTransition> {
        val transitions = mutableListOf<ActivityTransition>()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()

        return transitions
    }

    private fun initPendingIntent(): PendingIntent {
        val intent = Intent(TRANSITION_ACTION_RECEIVER)
        return PendingIntent.getBroadcast(applicationContext, 0, intent, FLAG_UPDATE_CURRENT);
    }

    companion object {
        const val TAG = "LocationTransitions"
        const val TRANSITION_ACTION_RECEIVER = "TRANSITION_ACTION_RECEIVER";
    }
}