package com.example.citysearch.commands

import androidx.fragment.app.FragmentManager
import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.factories.CityDetailsFragmentFactory
import com.example.citysearch.viewmodels.CellTapCommand

/**
 * Command for opening a details screen for a search result.
 */
interface OpenDetailsCommand: CellTapCommand

/**
 * Implements the open details command using a fragment manager.
 *
 * Handles the necessary fragment transactions and creates the right details fragment
 */
class OpenDetailsCommandImp(private val fragmentManager: FragmentManager,
                            private val detailsFragmentFactory: CityDetailsFragmentFactory,
                            private val searchResult: CitySearchResult
):
    OpenDetailsCommand {

    override fun invoke() {

        val detailsView = detailsFragmentFactory.detailsFragment(searchResult)
        val transaction = fragmentManager.beginTransaction()

        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        transaction.replace(android.R.id.content, detailsView)
            .addToBackStack(null)
            .commit()

        fragmentManager.executePendingTransactions()
    }
}