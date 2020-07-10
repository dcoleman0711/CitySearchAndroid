package com.example.citysearch.stub

import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.entities.CitySearchResults
import com.example.citysearch.entities.GeoPoint

class CitySearchResultsStub {

    companion object {

        fun stubResults(): CitySearchResults {

            val stubItemCount = 20
            val itemIndices = 0 until stubItemCount

            val items = itemIndices.map { index ->

                val stubName = "Stub City #$index"
                CitySearchResult(
                    stubName,
                    (index + 1) * 1000,
                    GeoPoint(
                        index.toDouble() * 20.0,
                        index.toDouble() * 40.0
                    ),
                    adminCode = "QZ"
                )

            }

            return CitySearchResults(items)
        }
    }
}