package com.example.citysearch.data

// Data class for the results of an image search.  Contains a list of images
data class ImageSearchResults(val images_results: List<ImageSearchResult>)

// Data class for an image search result entity.  The only property we're really intereste in is "original", which is the URL link to the original image
data class ImageSearchResult(val original: String?)