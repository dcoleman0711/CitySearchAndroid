package com.example.citysearch.search.searchresults

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citysearch.reactive.RecyclerViewBinder
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultCell
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModel
import io.reactivex.disposables.Disposable

interface SearchResultsView {

    val view: View
}

class SearchResultsViewImp(context: Context, private val viewModel: SearchResultsViewModel): SearchResultsView {

    override val view: View get() = recyclerView

    constructor(context: Context, model: SearchResultsModel) : this(context, SearchResultsViewModelImp(context, model))

    private val recyclerView: RecyclerView
    private val binder: RecyclerViewBinder<CitySearchResultViewModel, CitySearchResultCell> = RecyclerViewBinder(::CitySearchResultCell)

    private val recyclerViewBinding: Disposable

    init {

        recyclerView = RecyclerView(context)
        recyclerViewBinding = binder.bindCells(recyclerView, viewModel.resultsViewModels)

        setupView()
    }

    private fun setupView() {

        recyclerView.setBackgroundColor(Color.TRANSPARENT)
        recyclerView.isHorizontalScrollBarEnabled = false

        val layoutManager = GridLayoutManager(recyclerView.context, 2, GridLayoutManager.HORIZONTAL, false)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
    }
}