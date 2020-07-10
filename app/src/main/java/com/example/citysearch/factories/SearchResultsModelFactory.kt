package com.example.citysearch.factories

import com.example.citysearch.models.SearchResultsModel
import com.example.citysearch.models.SearchResultsModelImp

/**
 * Factory for creating SearchResultsModels.  Useful for testing.
 */
interface SearchResultsModelFactory {

    fun searchResultsModel(openDetailsCommandFactory: OpenDetailsCommandFactory): SearchResultsModel
}

class SearchResultsModelFactoryImp:
    SearchResultsModelFactory {

    override fun searchResultsModel(openDetailsCommandFactory: OpenDetailsCommandFactory): SearchResultsModel {

        return SearchResultsModelImp(
            openDetailsCommandFactory
        )
    }
}