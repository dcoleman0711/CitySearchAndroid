package com.example.citysearch.search.searchresults.citysearchresultcell

import android.content.Context

interface CitySearchResultViewModelFactory {

    fun resultViewModel(context: Context, model: CitySearchResultModel): CitySearchResultViewModel
}

class CitySearchResultViewModelFactoryImp: CitySearchResultViewModelFactory {

    override fun resultViewModel(context: Context, model: CitySearchResultModel): CitySearchResultViewModel {

        return CitySearchResultViewModelImp(context, model)
    }
}