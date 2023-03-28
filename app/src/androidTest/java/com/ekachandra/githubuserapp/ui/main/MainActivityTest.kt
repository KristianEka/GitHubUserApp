package com.ekachandra.githubuserapp.ui.main

import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.ekachandra.githubuserapp.R
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val dummyUsername = "KristianEka"

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun test_listUserVisible() {
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        onView(withId(R.id.rvUsers)).check(matches(isDisplayed()))


    }

    fun searchView() {
        onView(withId(R.id.search)).perform(
            click(), typeText(dummyUsername), pressKey(KeyEvent.KEYCODE_ENTER)
        )

        onData(withText(dummyUsername)).inAdapterView(withId(R.id.rvUsers)).check(
            matches(
                isDisplayed()
            )
        )
    }

    fun themeChanger() {
        onView(withId(R.id.theme_changer)).perform(click())
    }

    fun favoriteIntent() {
        onView(withId(R.id.favorite)).perform(click())
    }
}