package com.example.citysearch.factories

import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.fragments.CityDetailsFragment

/**
 * Factory for creating CityDetailsFragments.  Useful for testing
 */
interface CityDetailsFragmentFactory {

    fun detailsFragment(searchResult: CitySearchResult): CityDetailsFragment
}

class CityDetailsFragmentFactoryImp:
    CityDetailsFragmentFactory {

    override fun detailsFragment(searchResult: CitySearchResult): CityDetailsFragment {

        return CityDetailsFragment(searchResult)
    }
}