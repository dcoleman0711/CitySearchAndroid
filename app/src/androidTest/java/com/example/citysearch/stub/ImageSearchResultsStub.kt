package com.example.citysearch.stub

import com.example.citysearch.entities.ImageSearchResult
import com.example.citysearch.entities.ImageSearchResults

class ImageSearchResultsStub {

    companion object {

        fun stubResults(): ImageSearchResults {

            return ImageSearchResults(
                arrayListOf(
                    ImageSearchResult("https://images.com/image1"),
                    ImageSearchResult("https://images.com/image2"),
                    ImageSearchResult("https://images.com/image3"),
                    ImageSearchResult("https://images.com/image4"),
                    ImageSearchResult("https://images.com/image5")
                )
            )
        }
    }
}