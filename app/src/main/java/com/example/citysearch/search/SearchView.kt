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
import com.example.citysearch.utilities.ViewUtilities

class SearchView(context: Context, searchResultsModel: SearchResultsModel): Fragment() {

    private val searchResultsView: SearchResultsView
    private val parallaxView: ParallaxView

    private val viewModel: SearchViewModel

    private val view: ConstraintLayout

    init {

        val searchResultsViewModel = SearchResultsViewModelImp(context, searchResultsModel)
        searchResultsView = SearchResultsViewImp(context, searchResultsViewModel)

        val parallaxModel = ParallaxModelImp()
        val parallaxViewModel = ParallaxViewModelImp(parallaxModel)
        parallaxView = ParallaxViewImp(context, parallaxViewModel)

        val model = SearchModelImp(context, parallaxModel, searchResultsModel)
        viewModel = SearchViewModelImp(model, parallaxViewModel, searchResultsViewModel)

        view = ConstraintLayout(context)

        setupView()
        buildLayout(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return view
    }

    private fun setupView() {

        view.id = R.id.view
        view.setBackgroundColor(Color.WHITE)

        parallaxView.view.id = R.id.parallaxView
        view.addView(parallaxView.view)

        searchResultsView.view.id = R.id.searchResults
        view.addView(searchResultsView.view)
    }

    private fun buildLayout(context: Context) {

        val constraints = ConstraintSet()

        // Parallax View
        constraints.connect(parallaxView.view.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
        constraints.connect(parallaxView.view.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
        constraints.connect(parallaxView.view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        constraints.connect(parallaxView.view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)

        // Search Results
        constraints.connect(searchResultsView.view.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
        constraints.connect(searchResultsView.view.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
        constraints.centerVertically(searchResultsView.view.id, ConstraintSet.PARENT_ID)
        constraints.constrainHeight(searchResultsView.view.id, View.MeasureSpec.makeMeasureSpec(ViewUtilities.convertToPixels(context, 312), View.MeasureSpec.EXACTLY))

        constraints.applyTo(view)
    }
}