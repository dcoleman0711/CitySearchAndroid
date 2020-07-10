package com.example.citysearch.details.imagecarousel.asyncimage

import android.content.Context
import com.example.citysearch.data.ImageServiceImp
import java.net.URL

/**
 * Factory for creating AsyncImageModels.
 *
 * Useful for mocking model creation during tests
 */
interface AsyncImageModelFactory {

    fun imageModel(url: URL): AsyncImageModel
}

class AsyncImageModelFactoryImp(private val context: Context): AsyncImageModelFactory {

    override fun imageModel(url: URL): AsyncImageModel {

        return AsyncImageModelImp(context, url, ImageServiceImp())
    }
}