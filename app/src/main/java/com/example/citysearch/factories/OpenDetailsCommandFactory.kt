package com.example.citysearch.factories

import androidx.fragment.app.FragmentManager
import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.commands.OpenDetailsCommand
import com.example.citysearch.commands.OpenDetailsCommandImp

/**
 * Factory for creating OpenDetailsCommands.  Useful for testing.
 */
interface OpenDetailsCommandFactory {

    fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand
}

class OpenDetailsCommandFactoryImp(private val fragmentManager: FragmentManager,
                                   private val detailsFragmentFactory: CityDetailsFragmentFactory
): OpenDetailsCommandFactory {

    override fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand {

        return OpenDetailsCommandImp(
            fragmentManager,
            detailsFragmentFactory,
            searchResult
        )
    }
}