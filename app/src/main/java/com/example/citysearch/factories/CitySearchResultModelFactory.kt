package com.example.citysearch.factories

import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.models.CitySearchResultModel
import com.example.citysearch.models.CitySearchResultModelImp

/**
 * Factory for creating CitySearchResultModels.  Useful for testing.
 */
interface CitySearchResultModelFactory {

    fun resultModel(searchResult: CitySearchResult, openDetailsCommandFactory: OpenDetailsCommandFactory): CitySearchResultModel
}

class CitySearchResultModelFactoryImp:
    CitySearchResultModelFactory {

    override fun resultModel(searchResult: CitySearchResult, openDetailsCommandFactory: OpenDetailsCommandFactory): CitySearchResultModel {

        return CitySearchResultModelImp(
            searchResult,
            openDetailsCommandFactory
        )
    }
}