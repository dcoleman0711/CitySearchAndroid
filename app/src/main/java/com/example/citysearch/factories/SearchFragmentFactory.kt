package com.example.citysearch.factories

import com.example.citysearch.models.SearchResultsModel
import com.example.citysearch.fragments.SearchFragment

/**
 * Factory for creating SearchFragments.  Useful for testing.
 */
interface SearchFragmentFactory {

    fun searchFragment(searchResultsModel: SearchResultsModel): SearchFragment
}

class SearchFragmentFactoryImp:
    SearchFragmentFactory {

    override fun searchFragment(searchResultsModel: SearchResultsModel): SearchFragment {

        return SearchFragment(searchResultsModel)
    }
}