package com.example.citysearch.search

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.CityDetailsViewFactory
import com.example.citysearch.reactive.CellTapCommand

interface OpenDetailsCommand: CellTapCommand

class OpenDetailsCommandImp(private val context: Context,
                            private val fragmentManager: FragmentManager,
                            private val detailsViewFactory: CityDetailsViewFactory,
                            private val searchResult: CitySearchResult): OpenDetailsCommand {

    override fun invoke() {

        val detailsView = detailsViewFactory.detailsView(context, searchResult)
        val transaction = fragmentManager.beginTransaction()

        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        transaction.replace(android.R.id.content, detailsView)
            .addToBackStack(null)
            .commit()

        fragmentManager.executePendingTransactions()
    }
}