package com.example.citysearch.viewmodels

import androidx.lifecycle.ViewModel
import com.example.citysearch.models.SearchModel

/**
 * ViewModel for Search MVVM.
 *
 * Connects the parallax view-model to the search results view-model, so that the parallax layers scroll with the search results
 */
interface SearchViewModel

class SearchViewModelImp(
    private val model: SearchModel, // Held on this class to keep it alive
    private val parallaxViewModel: ParallaxViewModel,
    private val searchResultsViewModel: SearchResultsViewModel
): SearchViewModel, ViewModel() {

    init {

        connectParallaxToSearchResults()
    }

    private fun connectParallaxToSearchResults() {

        parallaxViewModel.contentOffset = searchResultsViewModel.contentOffset
    }
}