package com.example.citysearch.startup

import android.app.Activity
import android.content.Context
import android.os.Bundle

class StartupActivity: Activity() {

    companion object {

        lateinit var context: Context
    }

    val startupViewBuilder = StartupViewBuilder()

    private lateinit var startupView: StartupView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        context = this

        startupView = startupViewBuilder.build(this)
        setContentView(startupView.view)
    }
}