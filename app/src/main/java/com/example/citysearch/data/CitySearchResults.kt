package com.example.citysearch.data

// Data class for the results of a city search.  Contains a list of city entities
data class CitySearchResults(val results: List<CitySearchResult>) {

    companion object {

        fun emptyResults() = CitySearchResults(arrayListOf())
    }
}