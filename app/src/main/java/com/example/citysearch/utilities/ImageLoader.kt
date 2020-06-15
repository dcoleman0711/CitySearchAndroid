package com.example.citysearch.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ImageLoader {

    companion object {

        fun loadImage(context: Context, name: String): Bitmap {

            val fileStream = context.assets.open(name)
            return BitmapFactory.decodeStream(fileStream)
        }
    }
}