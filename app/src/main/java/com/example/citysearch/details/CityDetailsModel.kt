package com.example.citysearch.details

import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.data.ImageSearchService
import com.example.citysearch.data.ImageSearchServiceImp
import com.example.citysearch.details.imagecarousel.ImageCarouselModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.http.Url
import java.net.URL
import java.net.URLDecoder

interface CityDetailsModel {

    val title: Observable<String>
    val population: Observable<Int>
    val loading: Observable<Boolean>
}

class CityDetailsModelImp(private val searchResult: CitySearchResult,
                          private val imageCarouselModel: ImageCarouselModel,
                          private val imageSearchService: ImageSearchService,
                          private val resultsQueue: Scheduler): CityDetailsModel {

    override val title: Observable<String>
    override val population: Observable<Int>
    override val loading: Observable<Boolean>

    private val maxImageResults = 20

    constructor(searchResult: CitySearchResult, imageCarouselModel: ImageCarouselModel) : this(searchResult, imageCarouselModel, ImageSearchServiceImp(), Schedulers.computation())

    init {

        title = Observable.just(searchResult.nameAndState)
        population = Observable.just(searchResult.population)

        val imageSearch = imageSearchService.imageSearch(searchResult.nameAndState)
        val imageURLEvents = imageSearch.map { results ->
            results.images_results
                .mapNotNull { result ->
                    result.original
                }
                .map { urlStr ->
                    URL(urlStr)
                }
                .take(maxImageResults)
        }.observeOn(resultsQueue)

        loading = Observable.just(true)
            .mergeWith(imageURLEvents.map { false })

        imageCarouselModel.results = imageURLEvents
    }
}