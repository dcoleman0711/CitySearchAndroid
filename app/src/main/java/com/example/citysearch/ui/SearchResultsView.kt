package com.example.citysearch.ui

import android.content.Context
import android.graphics.Point
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.citysearch.R
import com.example.citysearch.reactive.RecyclerViewBinder
import com.example.citysearch.reactive.RecyclerViewBinderImp
import com.example.citysearch.viewmodels.SearchResultsViewModel
import com.example.citysearch.viewmodels.CitySearchResultViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * View for SearchResults MVVM.
 *
 * Binds the RecyclerView to the view-model, and passes its content offset back to the view-model as an observable stream
 */
interface SearchResultsView {

    val view: View
}

class SearchResultsViewImp(
    context: Context,
    override val view: RecyclerView,
    private val viewModel: SearchResultsViewModel
): SearchResultsView {

    private val binder = RecyclerViewBinderImp(context, R.layout.citysearchresultcell, ::CitySearchResultCell)

    private val recyclerViewBinding: Disposable

    init {

        recyclerViewBinding = binder.bindCells(view, viewModel.resultsViewModels)

        viewModel.provideContentOffset(Observable.create { emitter ->

            val point = Point(0, 0)

            view.addOnScrollListener(object: RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    point.x += dx
                    point.y += dy

                    emitter.onNext(point)
                }
            })
        })
    }
}