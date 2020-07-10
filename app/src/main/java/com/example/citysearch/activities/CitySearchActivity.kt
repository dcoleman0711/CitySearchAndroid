package com.example.citysearch.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.citysearch.fragments.StartupFragment

/**
 * Main application activity
 *
 * The whole app uses a single activity.  Navigation is handled with fragments.
 */
class CitySearchActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val startupView = StartupFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, startupView, "startupView")
            .commitNow()
    }
}