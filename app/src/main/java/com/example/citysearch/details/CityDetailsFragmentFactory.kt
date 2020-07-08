package com.example.citysearch.details

import com.example.citysearch.data.CitySearchResult

// Factory for creating CityDetailsFragments.  Useful for testing
interface CityDetailsFragmentFactory {

    fun detailsFragment(searchResult: CitySearchResult): CityDetailsFragment
}

class CityDetailsFragmentFactoryImp: CityDetailsFragmentFactory {

    override fun detailsFragment(searchResult: CitySearchResult): CityDetailsFragment {

        return CityDetailsFragment(searchResult)
    }
}