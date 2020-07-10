package com.example.citysearch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.models.CityDetailsModel
import com.example.citysearch.viewmodels.CityDetailsViewModelImp

/**
 * JetPack factory for CityDetailsViewModel, to support passing in a model
 */
class CityDetailsViewModelFactory(private val model: CityDetailsModel): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return CityDetailsViewModelImp(model) as T
    }
}