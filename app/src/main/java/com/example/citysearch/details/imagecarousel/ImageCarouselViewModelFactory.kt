package com.example.citysearch.details.imagecarousel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * JetPack factory for ImageCarouselViewModel, to support passing in a model
 */
class ImageCarouselViewModelFactory(private val model: ImageCarouselModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return ImageCarouselViewModelImp(model) as T
    }
}