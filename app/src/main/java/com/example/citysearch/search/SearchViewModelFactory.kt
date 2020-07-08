package com.example.citysearch.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.parallax.ParallaxViewModel
import com.example.citysearch.search.searchresults.SearchResultsViewModel

class SearchViewModelFactory(private val model: SearchModel, private val parallaxViewModel: ParallaxViewModel, private val searchResultsViewModel: SearchResultsViewModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return SearchViewModelImp(model, parallaxViewModel, searchResultsViewModel) as T
    }
}