package com.ama

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.ama.presentation.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConfigurationFragmentTest {

    @Rule
    @JvmField
    val rule = object : ActivityTestRule<MainActivity>(MainActivity::class.java) {
        override fun beforeActivityLaunched() {
            InstrumentationRegistry.getInstrumentation()
                .targetContext
                .getSharedPreferences(AmaApplication.AMA_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .remove(AmaApplication.CONFIGURATION_ID).apply()
            super.beforeActivityLaunched()
        }
    }

    @Test
    fun configurationFragmentShouldBeDisplayedProperly_whenInitializationIsDone() {
        onView(withId(R.id.et_configuration_id)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_configuration)).check(matches(isDisplayed()))
    }
}