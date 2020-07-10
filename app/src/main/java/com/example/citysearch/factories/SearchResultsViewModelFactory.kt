package com.example.citysearch.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.models.SearchResultsModel
import com.example.citysearch.viewmodels.SearchResultsViewModelImp

/**
 * JetPack factory for SearchResultsViewModel, to support passing in the context and a model
 */
class SearchResultsViewModelFactory(
    private val context: Context,
    private val model: SearchResultsModel
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return SearchResultsViewModelImp(
            context,
            model
        ) as T
    }
}