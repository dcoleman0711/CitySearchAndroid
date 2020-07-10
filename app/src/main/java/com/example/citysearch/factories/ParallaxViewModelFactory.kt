package com.example.citysearch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.models.ParallaxModel
import com.example.citysearch.viewmodels.ParallaxViewModelImp

/**
 * JetPack factory for ParallaxViewModel, to support passing in a model
 */
class ParallaxViewModelFactory(private val model: ParallaxModel): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return ParallaxViewModelImp(model) as T
    }
}