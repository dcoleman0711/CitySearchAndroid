package com.example.citysearch.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CityDetailsViewModelFactory(private val model: CityDetailsModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return CityDetailsViewModelImp(model) as T
    }
}