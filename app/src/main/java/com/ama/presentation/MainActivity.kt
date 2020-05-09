package com.ama.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ama.R
import com.ama.presentation.location.view.LocationOnBack

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        getLocationOnBack()?.onBackPressed()
        super.onBackPressed()
    }

    private fun getLocationOnBack(): LocationOnBack? {
        return supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.find { it is LocationOnBack }
            ?.let { it as LocationOnBack }
    }
}
