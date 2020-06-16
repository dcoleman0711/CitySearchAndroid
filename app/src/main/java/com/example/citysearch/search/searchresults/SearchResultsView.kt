package com.example.citysearch.search.searchresults

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citysearch.reactive.RecyclerViewBinder
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultCell
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModel
import com.example.citysearch.utilities.Point
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface SearchResultsView {

    val view: View
}

class SearchResultsViewImp(context: Context, private val viewModel: SearchResultsViewModel): SearchResultsView {

    override val view: RecyclerView

    private val binder: RecyclerViewBinder<CitySearchResultViewModel, CitySearchResultCell> = RecyclerViewBinder(::CitySearchResultCell)

    private val recyclerViewBinding: Disposable

    init {

        view = RecyclerView(context)
        recyclerViewBinding = binder.bindCells(view, viewModel.resultsViewModels)

        viewModel.contentOffset = Observable.create { emitter ->

            val point = Point(0, 0)

            view.addOnScrollListener(object: RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    point.x += dx
                    point.y += dy

                    emitter.onNext(point)
                }
            })
        }

        setupView()
    }

    private fun setupView() {

        view.setBackgroundColor(Color.TRANSPARENT)
        view.isHorizontalScrollBarEnabled = false

        val layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.HORIZONTAL, false)
        layoutManager.orientation = GridLayoutManager.HORIZONTAL
        view.layoutManager = layoutManager
    }
}