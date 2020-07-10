package com.example.citysearch.factories

import android.content.Context
import com.example.citysearch.services.ImageServiceImp
import com.example.citysearch.models.AsyncImageModel
import com.example.citysearch.models.AsyncImageModelImp
import java.net.URL

/**
 * Factory for creating AsyncImageModels.
 *
 * Useful for mocking model creation during tests
 */
interface AsyncImageModelFactory {

    fun imageModel(url: URL): AsyncImageModel
}

class AsyncImageModelFactoryImp(private val context: Context):
    AsyncImageModelFactory {

    override fun imageModel(url: URL): AsyncImageModel {

        return AsyncImageModelImp(
            context,
            url,
            ImageServiceImp()
        )
    }
}