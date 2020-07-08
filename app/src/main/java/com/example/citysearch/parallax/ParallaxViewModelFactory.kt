package com.example.citysearch.parallax

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ParallaxViewModelFactory(private val model: ParallaxModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return ParallaxViewModelImp(model) as T
    }
}