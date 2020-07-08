package com.example.citysearch.search

import android.content.Context
import com.example.citysearch.search.searchresults.SearchResultsModel

interface SearchFragmentFactory {

    fun searchFragment(context: Context, searchResultsModel: SearchResultsModel): SearchFragment
}

class SearchFragmentFactoryImp: SearchFragmentFactory {

    override fun searchFragment(context: Context, searchResultsModel: SearchResultsModel): SearchFragment {

        return SearchFragment(context, searchResultsModel)
    }
}