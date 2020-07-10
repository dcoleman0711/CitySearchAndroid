package com.example.citysearch.entities

/**
 * Data class for the results of an image search.  Contains a list of images (JSON codable)
 *
 * @property images_results The list of images returned by the search
 */
data class ImageSearchResults(
    val images_results: List<ImageSearchResult>
)

/**
 * Data class for an image search result entity. (JSON codable)
 *
 * @property original The original image URL
 */
data class ImageSearchResult(
    val original: String?
)