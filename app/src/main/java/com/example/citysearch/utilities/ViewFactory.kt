package com.example.citysearch.utilities

import android.content.Context
import com.example.citysearch.animations.RollingAnimationLabel

interface ViewFactory {

    fun rollingAnimationLabel(context: Context): RollingAnimationLabel
}

class ViewFactoryImp: ViewFactory {

    override fun rollingAnimationLabel(context: Context): RollingAnimationLabel {

        return RollingAnimationLabel(context)
    }
}