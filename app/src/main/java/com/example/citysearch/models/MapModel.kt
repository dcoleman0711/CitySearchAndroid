package com.example.citysearch.models

import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.entities.GeoPoint
import io.reactivex.Observable

/**
 * Model for Map MVVM.
 *
 * Exposes the geo-location of the pin
 */
interface MapModel {

    val geoCoordinates: Observable<GeoPoint>
}

class MapModelImp(private val searchResult: CitySearchResult):
    MapModel {

    override val geoCoordinates = Observable.just(searchResult.location)
}