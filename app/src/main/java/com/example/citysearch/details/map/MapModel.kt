package com.example.citysearch.details.map

import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.data.GeoPoint
import io.reactivex.Observable

// Model for Map MVVM.  Exposes the geo-location of the pin
interface MapModel {

    val geoCoordinates: Observable<GeoPoint>
}

class MapModelImp(private val searchResult: CitySearchResult): MapModel {

    override val geoCoordinates: Observable<GeoPoint>

    init {

        geoCoordinates = Observable.just(searchResult.location)
    }
}