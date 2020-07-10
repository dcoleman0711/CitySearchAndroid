package com.example.citysearch.details.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * JetPack factory for MapViewModel, to support passing in a model
 */
class MapViewModelFactory(private val context: Context, private val model: MapModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return MapViewModelImp(context, model) as T
    }
}