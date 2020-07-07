package com.example.citysearch.search

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.parallax.ParallaxModelImp
import com.example.citysearch.parallax.ParallaxView
import com.example.citysearch.parallax.ParallaxViewImp
import com.example.citysearch.parallax.ParallaxViewModelImp
import com.example.citysearch.search.searchresults.*
import com.example.citysearch.utilities.*

class SearchView(private val view: ConstraintLayout,
                 private val viewModel: SearchViewModel,
                 private val searchResultsView: SearchResultsView,
                 private val parallaxView: ParallaxView): Fragment() {

    companion object {

        fun searchView(context: Context, searchResultsModel: SearchResultsModel): SearchView {

            val view = LayoutInflater.from(context).inflate(R.layout.search, null) as ConstraintLayout

            val parallaxModel = ParallaxModelImp()
            val parallaxViewModel = ParallaxViewModelImp(parallaxModel)
            val parallaxView = ParallaxViewImp(context, view.findViewById(R.id.parallaxView), parallaxViewModel)

            val searchResultsViewModel = SearchResultsViewModelImp(context, searchResultsModel)
            val searchResultsView = SearchResultsViewImp(context, view.findViewById(R.id.searchResults), searchResultsViewModel)

            val model = SearchModelImp(context, parallaxModel, searchResultsModel)
            val viewModel = SearchViewModelImp(model, parallaxViewModel, searchResultsViewModel)

            return SearchView(view, viewModel, searchResultsView, parallaxView)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return view
    }
}