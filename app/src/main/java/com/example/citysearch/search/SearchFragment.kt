package com.example.citysearch.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.citysearch.R
import com.example.citysearch.parallax.*
import com.example.citysearch.search.searchresults.*

// Fragment for Search.  Handles building the various components and managing the lifecycle
open class SearchFragment(private val searchResultsModel: SearchResultsModel): Fragment() {

    private lateinit var parallaxViewModel: ParallaxViewModel
    private lateinit var searchResultsViewModel: SearchResultsViewModel

    private lateinit var viewModel: SearchViewModel

    private var searchView: SearchView? = null

    override fun onAttach(context: Context) {

        super.onAttach(context)

        val parallaxModel = ParallaxModelImp()
        val parallaxViewModel: ParallaxViewModelImp by viewModels { ParallaxViewModelFactory(parallaxModel) }

        val searchResultsViewModel: SearchResultsViewModelImp by viewModels { SearchResultsViewModelFactory(context, searchResultsModel) }

        val model = SearchModelImp(context, parallaxModel, searchResultsModel)

        val viewModel: SearchViewModelImp by viewModels { SearchViewModelFactory(model, parallaxViewModel, searchResultsViewModel) }

        this.parallaxViewModel = parallaxViewModel
        this.searchResultsViewModel = searchResultsViewModel
        this.viewModel =  viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val searchView = searchView
        if(searchView != null)
            return searchView.view

        val view = inflater.inflate(R.layout.search, null) as ConstraintLayout

        val context = requireContext()

        val parallaxView = ParallaxViewImp(context, view.findViewById(R.id.parallaxView), parallaxViewModel)
        val searchResultsView = SearchResultsViewImp(context, view.findViewById(R.id.searchResults), searchResultsViewModel)

        this.searchView = SearchViewImp(view, viewModel, searchResultsView, parallaxView)

        return view
    }

    override fun onDestroy() {

        super.onDestroy()

        searchView = null
    }
}