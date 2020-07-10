package com.example.citysearch.search

import androidx.fragment.app.FragmentManager
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.CityDetailsFragmentFactory

/**
 * Factory for creating OpenDetailsCommands.  Useful for testing.
 */
interface OpenDetailsCommandFactory {

    fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand
}

class OpenDetailsCommandFactoryImp(private val fragmentManager: FragmentManager,
                                   private val detailsFragmentFactory: CityDetailsFragmentFactory): OpenDetailsCommandFactory {

    override fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand {

        return OpenDetailsCommandImp(fragmentManager, detailsFragmentFactory, searchResult)
    }
}