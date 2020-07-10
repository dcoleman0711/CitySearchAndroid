package com.example.citysearch.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.models.MapModel
import com.example.citysearch.viewmodels.MapViewModelImp

/**
 * JetPack factory for MapViewModel, to support passing in a model
 */
class MapViewModelFactory(private val context: Context, private val model: MapModel): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return MapViewModelImp(
            context,
            model
        ) as T
    }
}