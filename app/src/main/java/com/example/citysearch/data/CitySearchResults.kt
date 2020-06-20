package com.example.citysearch.data

import android.os.Parcel
import android.os.Parcelable

data class CitySearchResults(val results: List<CitySearchResult>) {

    companion object {

        fun emptyResults() = CitySearchResults(arrayListOf())
    }
}