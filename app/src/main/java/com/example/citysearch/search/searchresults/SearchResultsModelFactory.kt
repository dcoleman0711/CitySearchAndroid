package com.example.citysearch.search.searchresults

import com.example.citysearch.search.OpenDetailsCommandFactory

/**
 * Factory for creating SearchResultsModels.  Useful for testing.
 */
interface SearchResultsModelFactory {

    fun searchResultsModel(openDetailsCommandFactory: OpenDetailsCommandFactory): SearchResultsModel
}

class SearchResultsModelFactoryImp: SearchResultsModelFactory {

    override fun searchResultsModel(openDetailsCommandFactory: OpenDetailsCommandFactory): SearchResultsModel {

        return SearchResultsModelImp(openDetailsCommandFactory)
    }
}