package com.example.citysearch.details

import android.content.Context
import com.example.citysearch.data.CitySearchResult

interface CityDetailsFragmentFactory {

    fun detailsFragment(context: Context, searchResult: CitySearchResult): CityDetailsFragment
}

class CityDetailsFragmentFactoryImp: CityDetailsFragmentFactory {

    override fun detailsFragment(context: Context, searchResult: CitySearchResult): CityDetailsFragment {

        return CityDetailsFragment(context, searchResult)
    }
}