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
import com.example.citysearch.search.searchresults.SearchResultsModel
import com.example.citysearch.search.searchresults.SearchResultsModelImp
import com.example.citysearch.search.searchresults.SearchResultsView
import com.example.citysearch.search.searchresults.SearchResultsViewImp
import com.example.citysearch.utilities.ViewUtilities

class SearchView(context: Context, searchResultsModel: SearchResultsModel): Fragment() {

    private val searchResultsView: SearchResultsView
    private val viewModel: SearchViewModel

    private val view: ConstraintLayout

    init {

        searchResultsView = SearchResultsViewImp(context, searchResultsModel)
        viewModel = SearchViewModelImp()

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

        searchResultsView.view.id = R.id.searchResults
        view.addView(searchResultsView.view)
    }

    private fun buildLayout(context: Context) {

        val constraints = ConstraintSet()

        // Search Results
        constraints.connect(searchResultsView.view.id, ConstraintSet.LEFT, view.id, ConstraintSet.LEFT, 0)
        constraints.connect(searchResultsView.view.id, ConstraintSet.RIGHT, view.id, ConstraintSet.RIGHT, 0)
        constraints.centerVertically(searchResultsView.view.id, view.id)
        constraints.constrainHeight(searchResultsView.view.id, View.MeasureSpec.makeMeasureSpec(ViewUtilities.convertToPixels(context, 312), View.MeasureSpec.EXACTLY))

        constraints.applyTo(view)
    }
}