package com.example.citysearch.commands

import androidx.fragment.app.FragmentManager
import com.example.citysearch.entities.CitySearchResults
import com.example.citysearch.factories.*

/**
 * Command for transitioning from startup to search.
 */
interface StartupTransitionCommand {

    fun invoke(initialResults: CitySearchResults)
}

/**
 * Implements the startup transition with a fragment manager.
 *
 * Handles creating the search fragment and handling the fragment transactions
 */
class StartupTransitionCommandImp(
    private val fragmentManager: FragmentManager,
    private val searchResultsModelFactory: SearchResultsModelFactory,
    private val searchFragmentFactory: SearchFragmentFactory
): StartupTransitionCommand {

    constructor(fragmentManager: FragmentManager):
            this(
                fragmentManager,
                SearchResultsModelFactoryImp(),
                SearchFragmentFactoryImp()
            )

    override fun invoke(initialResults: CitySearchResults) {

        val openDetailsCommandFactory =
            OpenDetailsCommandFactoryImp(
                fragmentManager,
                CityDetailsFragmentFactoryImp()
            )
        val searchResultsModel = searchResultsModelFactory.searchResultsModel(openDetailsCommandFactory)
        searchResultsModel.setResults(initialResults)
        val searchView = searchFragmentFactory.searchFragment(searchResultsModel)

        val transaction = fragmentManager.beginTransaction()

        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        transaction.replace(android.R.id.content, searchView)
            .commit()
    }
}