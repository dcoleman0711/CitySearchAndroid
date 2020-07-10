package com.example.citysearch.data

/**
 * Data class for a single city entity returned by the city search service (JSON codable)
 *
 * @property name Name of the city
 * @property population Population of the city
 * @property location GeoPoint structure for the location on Earth of the city
 * @property adminCode Admin code for the city.  For U.S. cities, this is the two letter abbreviation for state
 */
data class CitySearchResult(val name: String, val population: Int, val location: GeoPoint, val adminCode: String) {

    val nameAndState: String get() = "${name}, ${adminCode}"
}

/**
 * Longitude-latitude data point used by the city search service (JSON codable)
 * @property latitude The latitute, in degrees
 * @property longitude The longitude, in degrees
 */
data class GeoPoint(val latitude: Double, val longitude: Double)