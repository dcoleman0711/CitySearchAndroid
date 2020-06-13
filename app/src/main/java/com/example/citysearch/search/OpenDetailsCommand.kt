package com.example.citysearch.search

import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.CityDetailsView
import com.example.citysearch.reactive.CellTapCommand

interface OpenDetailsCommand: CellTapCommand

class OpenDetailsCommandImp(private val searchRoot: SearchRoot, private val searchResult: CitySearchResult): OpenDetailsCommand {

    override fun invoke() {

        val detailsView = CityDetailsView(searchRoot, searchResult)
        val fragmentManager = searchRoot.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        transaction.replace(android.R.id.content, detailsView)
            .addToBackStack(null)
            .commit()

        fragmentManager.executePendingTransactions()
    }
}