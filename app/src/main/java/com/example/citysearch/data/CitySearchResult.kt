package com.example.citysearch.data

data class CitySearchResult(val name: String, val population: Int, val location: GeoPoint)

data class GeoPoint(val latitude: Double, val longitude: Double)