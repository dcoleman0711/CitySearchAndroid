package com.example.citysearch.utilities

import android.content.Context
import android.widget.ImageView
import com.example.citysearch.animations.RollingAnimationLabel

interface ViewFactory {

    fun imageView(): ImageView
}

class ViewFactoryImp(private val context: Context): ViewFactory {

    override fun imageView(): ImageView {

        return ImageView(context)
    }
}