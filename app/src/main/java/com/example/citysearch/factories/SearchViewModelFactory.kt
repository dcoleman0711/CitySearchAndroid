package com.example.citysearch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.models.SearchModel
import com.example.citysearch.viewmodels.ParallaxViewModel
import com.example.citysearch.viewmodels.SearchResultsViewModel
import com.example.citysearch.viewmodels.SearchViewModelImp

/**
 * JetPack factory for SearchViewModel, to support passing in the model and child view-models
 */
class SearchViewModelFactory(private val model: SearchModel,
                             private val parallaxViewModel: ParallaxViewModel,
                             private val searchResultsViewModel: SearchResultsViewModel
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return SearchViewModelImp(
            model,
            parallaxViewModel,
            searchResultsViewModel
        ) as T
    }
}