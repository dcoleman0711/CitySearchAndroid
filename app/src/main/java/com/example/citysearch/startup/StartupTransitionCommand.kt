package com.example.citysearch.startup

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.details.CityDetailsViewFactoryImp
import com.example.citysearch.search.*
import com.example.citysearch.search.searchresults.SearchResultsModelFactory
import com.example.citysearch.search.searchresults.SearchResultsModelFactoryImp
import com.example.citysearch.search.searchresults.SearchResultsModelImp

interface StartupTransitionCommand {

    fun invoke(initialResults: CitySearchResults)
}

class StartupTransitionCommandImp(private val context: Context,
                                  private val fragmentManager: FragmentManager,
                                  private val searchResultsModelFactory: SearchResultsModelFactory,
                                  private val searchFragmentFactory: SearchFragmentFactory): StartupTransitionCommand {

    constructor(context: Context,
                fragmentManager: FragmentManager):
            this(
                context,
                fragmentManager,
                SearchResultsModelFactoryImp(),
                SearchFragmentFactoryImp()
            )

    override fun invoke(initialResults: CitySearchResults) {

        val openDetailsCommandFactory = OpenDetailsCommandFactoryImp(context, fragmentManager, CityDetailsViewFactoryImp())
        val searchResultsModel = searchResultsModelFactory.searchResultsModel(openDetailsCommandFactory)
        searchResultsModel.setResults(initialResults)
        val searchView = searchFragmentFactory.searchFragment(context, searchResultsModel)

        val transaction = fragmentManager.beginTransaction()

        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        transaction.replace(android.R.id.content, searchView)
            .commit()
    }
}