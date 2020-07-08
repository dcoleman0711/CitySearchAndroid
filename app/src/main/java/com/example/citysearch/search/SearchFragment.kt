package com.example.citysearch.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.citysearch.R
import com.example.citysearch.parallax.ParallaxModelImp
import com.example.citysearch.parallax.ParallaxViewImp
import com.example.citysearch.parallax.ParallaxViewModelImp
import com.example.citysearch.search.searchresults.SearchResultsModel
import com.example.citysearch.search.searchresults.SearchResultsViewImp
import com.example.citysearch.search.searchresults.SearchResultsViewModelImp

class SearchFragment(context: Context, private val searchResultsModel: SearchResultsModel): Fragment() {

    private val parallaxViewModel: ParallaxViewModelImp
    private val searchResultsViewModel: SearchResultsViewModelImp

    private val viewModel: SearchViewModelImp

    private lateinit var searchView: SearchView

    init {

        val parallaxModel = ParallaxModelImp()
        parallaxViewModel = ParallaxViewModelImp(parallaxModel)

        searchResultsViewModel = SearchResultsViewModelImp(context, searchResultsModel)

        val model = SearchModelImp(context, parallaxModel, searchResultsModel)
        viewModel = SearchViewModelImp(model, parallaxViewModel, searchResultsViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.search, null) as ConstraintLayout

        val context = requireContext()

        val parallaxView = ParallaxViewImp(context, view.findViewById(R.id.parallaxView), parallaxViewModel)
        val searchResultsView = SearchResultsViewImp(context, view.findViewById(R.id.searchResults), searchResultsViewModel)
        searchView = SearchViewImp(view, viewModel, searchResultsView, parallaxView)

        return view
    }
}