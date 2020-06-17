package com.example.citysearch.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent

interface IntentFactory {

    fun<T: Activity> intent(context: Context, activityClass: Class<T>): Intent
}

class IntentFactoryImp: IntentFactory {

    override fun<T: Activity> intent(context: Context, activityClass: Class<T>): Intent {

        return Intent(context, activityClass)
    }
}