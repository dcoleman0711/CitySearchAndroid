package com.example.citysearch.parallax

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * JetPack factory for ParallaxViewModel, to support passing in a model
 */
class ParallaxViewModelFactory(private val model: ParallaxModel): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return ParallaxViewModelImp(model) as T
    }
}