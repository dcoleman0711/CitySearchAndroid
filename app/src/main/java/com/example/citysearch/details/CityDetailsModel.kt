package com.example.citysearch.details

import com.example.citysearch.data.CitySearchResult
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface CityDetailsModel {

    val title: Observable<String>
    val population: Observable<Int>
    val loading: Observable<Boolean>
}

class CityDetailsModelImp(private val searchResult: CitySearchResult): CityDetailsModel {

    override val title: Observable<String>
    override val population: Observable<Int>
    override val loading: BehaviorSubject<Boolean>

    init {

        title = Observable.just(searchResult.name)
        population = Observable.just(searchResult.population)

        loading = BehaviorSubject.create()
        loading.onNext(true)
    }
}