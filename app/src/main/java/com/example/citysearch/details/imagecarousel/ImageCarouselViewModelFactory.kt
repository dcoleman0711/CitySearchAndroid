package com.example.citysearch.details.imagecarousel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageCarouselViewModelFactory(private val model: ImageCarouselModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return ImageCarouselViewModelImp(model) as T
    }
}