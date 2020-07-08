package com.example.citysearch.search.searchresults.citysearchresultcell

import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.search.OpenDetailsCommandFactory

// Factory for creating CitySearchResultModels.  Useful for testing.
interface CitySearchResultModelFactory {

    fun resultModel(searchResult: CitySearchResult, openDetailsCommandFactory: OpenDetailsCommandFactory): CitySearchResultModel
}

class CitySearchResultModelFactoryImp: CitySearchResultModelFactory {

    override fun resultModel(searchResult: CitySearchResult, openDetailsCommandFactory: OpenDetailsCommandFactory): CitySearchResultModel {

        return CitySearchResultModelImp(searchResult, openDetailsCommandFactory)
    }
}