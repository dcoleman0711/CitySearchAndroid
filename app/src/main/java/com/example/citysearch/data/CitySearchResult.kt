package com.example.citysearch.data

data class CitySearchResult(val name: String, val population: Int, val location: GeoPoint, val adminCode: String) {

    val nameAndState: String get() = "${name}, ${adminCode}"
}

data class GeoPoint(val latitude: Double, val longitude: Double)