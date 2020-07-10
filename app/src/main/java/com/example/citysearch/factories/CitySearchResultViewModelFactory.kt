package com.example.citysearch.factories

import android.content.Context
import com.example.citysearch.models.CitySearchResultModel
import com.example.citysearch.viewmodels.CitySearchResultViewModel
import com.example.citysearch.viewmodels.CitySearchResultViewModelImp

/**
 * Factory for creating CitySearchResultViewModels.  Useful for testing.
 */
interface CitySearchResultViewModelFactory {

    fun resultViewModel(context: Context, model: CitySearchResultModel): CitySearchResultViewModel
}

class CitySearchResultViewModelFactoryImp:
    CitySearchResultViewModelFactory {

    override fun resultViewModel(context: Context, model: CitySearchResultModel): CitySearchResultViewModel {

        return CitySearchResultViewModelImp(
            context,
            model
        )
    }
}