package com.example.citysearch.search

import com.example.citysearch.search.searchresults.SearchResultsModel

/**
 * Factory for creating SearchFragments.  Useful for testing.
 */
interface SearchFragmentFactory {

    fun searchFragment(searchResultsModel: SearchResultsModel): SearchFragment
}

class SearchFragmentFactoryImp: SearchFragmentFactory {

    override fun searchFragment(searchResultsModel: SearchResultsModel): SearchFragment {

        return SearchFragment(searchResultsModel)
    }
}