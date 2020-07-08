package com.example.citysearch.utilities

import android.content.Context
import android.widget.ImageView

// This makes it easier to test classes (i.e. the parallax view) that create image views on the fly
interface ViewFactory {

    fun imageView(): ImageView
}

class ViewFactoryImp(private val context: Context): ViewFactory {

    override fun imageView(): ImageView {

        return ImageView(context)
    }
}