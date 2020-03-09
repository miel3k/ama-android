package com.ama

import android.app.Application
import com.ama.di.DaggerApplicationComponent

class AmaApplication : Application() {

    val appComponent = DaggerApplicationComponent.create()
}