package com.example.citysearch.search.searchresults

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * JetPack factory for SearchResultsViewModel, to support passing in the context and a model
 */
class SearchResultsViewModelFactory(private val context: Context, private val model: SearchResultsModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return SearchResultsViewModelImp(context, model) as T
    }
}