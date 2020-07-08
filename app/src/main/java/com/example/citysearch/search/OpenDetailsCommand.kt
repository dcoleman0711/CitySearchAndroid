package com.example.citysearch.search

import androidx.fragment.app.FragmentManager
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.CityDetailsFragmentFactory
import com.example.citysearch.reactive.CellTapCommand

// Command for opening a details screen for a search result.  Handles the necessary fragment transactions and creates the right details fragment
interface OpenDetailsCommand: CellTapCommand

class OpenDetailsCommandImp(private val fragmentManager: FragmentManager,
                            private val detailsFragmentFactory: CityDetailsFragmentFactory,
                            private val searchResult: CitySearchResult): OpenDetailsCommand {

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