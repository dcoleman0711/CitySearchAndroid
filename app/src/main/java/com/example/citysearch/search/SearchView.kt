package com.example.citysearch.search

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.search.searchresults.SearchResultsModelImp
import com.example.citysearch.search.searchresults.SearchResultsView
import com.example.citysearch.search.searchresults.SearchResultsViewImp
import com.example.citysearch.utilities.ViewUtilities
import com.google.gson.Gson

class SearchView: Activity() {

    companion object {

        const val initialResultsKey = "initialResults"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val initialResults = Gson().fromJson(intent.getStringExtra(initialResultsKey), CitySearchResults::class.java)
        val searchResultsModel = SearchResultsModelImp()
        searchResultsModel.setResults(initialResults)

        searchResultsView = SearchResultsViewImp(this, searchResultsModel)
        viewModel = SearchViewModelImp()

        val view = ConstraintLayout(this)
        setContentView(view)

        setupView(view)
        buildLayout(view)
    }

    private fun setupView(view: ConstraintLayout) {

        view.id = R.id.view
        view.setBackgroundColor(Color.WHITE)

        searchResultsView.view.id = R.id.searchResults
        view.addView(searchResultsView.view)
    }

    private fun buildLayout(view: ConstraintLayout) {

        val constraints = ConstraintSet()

        // Search Results
        constraints.connect(searchResultsView.view.id, ConstraintSet.LEFT, view.id, ConstraintSet.LEFT, 0)
        constraints.connect(searchResultsView.view.id, ConstraintSet.RIGHT, view.id, ConstraintSet.RIGHT, 0)
        constraints.centerVertically(searchResultsView.view.id, view.id)
        constraints.constrainHeight(searchResultsView.view.id, View.MeasureSpec.makeMeasureSpec(ViewUtilities.convertToPixels(this, 312), View.MeasureSpec.EXACTLY))

        constraints.applyTo(view)
    }

    private lateinit var searchResultsView: SearchResultsView

    private lateinit var viewModel: SearchViewModel
}