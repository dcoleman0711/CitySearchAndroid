package com.example.citysearch.search

import com.example.citysearch.parallax.ParallaxViewModel
import com.example.citysearch.search.searchresults.SearchResultsViewModel

interface SearchViewModel {

}

class SearchViewModelImp(private val model: SearchModel, private val parallaxViewModel: ParallaxViewModel, private val searchResultsViewModel: SearchResultsViewModel): SearchViewModel {

    init {

        connectParallaxToSearchResults()
    }

    private fun connectParallaxToSearchResults() {

        parallaxViewModel.contentOffset = searchResultsViewModel.contentOffset
    }
}