package com.example.citysearch.details.map

import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.data.GeoPoint
import io.reactivex.Observable

interface MapModel {

    val geoCoordinates: Observable<GeoPoint>
}

class MapModelImp(private val searchResult: CitySearchResult): MapModel {

    override val geoCoordinates: Observable<GeoPoint>

    init {

        geoCoordinates = Observable.just(searchResult.location)
    }
}