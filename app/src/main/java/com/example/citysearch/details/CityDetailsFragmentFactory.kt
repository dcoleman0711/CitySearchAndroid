package com.example.citysearch.details

import android.content.Context
import com.example.citysearch.data.CitySearchResult

interface CityDetailsFragmentFactory {

    fun detailsFragment(searchResult: CitySearchResult): CityDetailsFragment
}

class CityDetailsFragmentFactoryImp: CityDetailsFragmentFactory {

    override fun detailsFragment(searchResult: CitySearchResult): CityDetailsFragment {

        return CityDetailsFragment(searchResult)
    }
}