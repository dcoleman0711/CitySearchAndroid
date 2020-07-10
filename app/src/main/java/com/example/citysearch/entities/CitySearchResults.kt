package com.example.citysearch.entities

/**
 * Data class for the results of a city search.  Contains a list of city entities (JSON codable)
 *
 * @property results The list of cities returned by the search
 */
data class CitySearchResults(val results: List<CitySearchResult>) {

    companion object {

        fun emptyResults() =
            CitySearchResults(arrayListOf())
    }
}