package com.example.citysearch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.models.ImageCarouselModel
import com.example.citysearch.viewmodels.ImageCarouselViewModelImp

/**
 * JetPack factory for ImageCarouselViewModel, to support passing in a model
 */
class ImageCarouselViewModelFactory(private val model: ImageCarouselModel): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return ImageCarouselViewModelImp(model) as T
    }
}