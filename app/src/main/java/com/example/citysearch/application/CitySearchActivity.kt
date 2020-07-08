package com.example.citysearch.application

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.citysearch.startup.StartupFragment

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