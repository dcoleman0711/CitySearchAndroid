package com.example.citysearch.utilities

import android.content.Context
import android.graphics.drawable.Drawable

class ImageLoader {

    companion object {

        fun loadImage(context: Context, name: String): Drawable {

            val fileStream = context.assets.open(name)
            return Drawable.createFromStream(fileStream, null)
        }
    }
}