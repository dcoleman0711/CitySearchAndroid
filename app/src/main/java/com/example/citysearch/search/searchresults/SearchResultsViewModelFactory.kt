package com.example.citysearch.search.searchresults

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchResultsViewModelFactory(private val context: Context, private val model: SearchResultsModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return SearchResultsViewModelImp(context, model) as T
    }
}