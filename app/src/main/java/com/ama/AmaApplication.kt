package com.ama

import com.ama.di.application.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

open class AmaApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }

    companion object {
        const val AMA_SHARED_PREFERENCES = "AMA_SHARED_PREFERENCES"
        const val CONFIGURATION_ID = "CONFIGURATION_ID"
    }
}