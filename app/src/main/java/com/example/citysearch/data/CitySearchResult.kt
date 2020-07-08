package com.example.citysearch.data

// Data class for a single city entity returned by the city search service (JSON codable)
data class CitySearchResult(val name: String, val population: Int, val location: GeoPoint, val adminCode: String) {

    val nameAndState: String get() = "${name}, ${adminCode}"
}

// Longitude-latitude data point used by the city search service (JSON codable)
data class GeoPoint(val latitude: Double, val longitude: Double)