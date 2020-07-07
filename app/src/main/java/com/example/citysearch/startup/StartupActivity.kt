package com.example.citysearch.startup

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.example.citysearch.R

class StartupActivity: Activity() {

    companion object {

        lateinit var context: Context
    }

    val startupViewBuilder = StartupViewBuilder()

    private lateinit var startupView: StartupView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        context = this

        setContentView(R.layout.startup)
        startupView = startupViewBuilder.build(this, findViewById(R.id.view))
    }
}