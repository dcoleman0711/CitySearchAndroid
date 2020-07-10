package com.example.citysearch.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * JetPack factory for CityDetailsViewModel, to support passing in a model
 */
class CityDetailsViewModelFactory(private val model: CityDetailsModel): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return CityDetailsViewModelImp(model) as T
    }
}