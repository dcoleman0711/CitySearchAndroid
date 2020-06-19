package com.example.citysearch.search

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.CityDetailsViewFactory

interface OpenDetailsCommandFactory {

    fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand
}

class OpenDetailsCommandFactoryImp(private val context: Context,
                                   private val fragmentManager: FragmentManager,
                                   private val detailsViewFactory: CityDetailsViewFactory): OpenDetailsCommandFactory {

    override fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand {

        return OpenDetailsCommandImp(context, fragmentManager, detailsViewFactory, searchResult)
    }
}