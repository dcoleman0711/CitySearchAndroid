package com.example.citysearch.startup

import android.app.Activity
import android.content.Context
import android.os.Bundle

class StartupActivity: Activity() {

    companion object {

        lateinit var context: Context
    }

    private lateinit var startupView: StartupView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        context = this

        startupView = StartupViewImp(this)
        setContentView(startupView.view)
    }
}