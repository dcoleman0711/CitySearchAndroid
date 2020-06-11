package com.example.citysearch.startup

import android.os.Looper
import android.util.Log
import com.example.citysearch.data.*
import io.reactivex.Observable
import io.reactivex.Observable.zip
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

interface StartupModel {

    val appTitle: Observable<String>

    fun startTransitionTimer()
}

class StartupModelImp(private val transitionCommand: StartupTransitionCommand, private val searchService: CitySearchService): StartupModel {

    constructor() : this(StartupTransitionCommandImp(), CitySearchServiceImp())

    override val appTitle = Observable.just("City Search")

    override fun startTransitionTimer() {

        val timer = Observable.timer(4, TimeUnit.SECONDS)

        val beginTransitionEvents = zip(initialResults, timer, BiFunction<CitySearchResults, Long, CitySearchResults> { initialResults, time -> initialResults })

        var subscriber: Disposable?
        subscriber = beginTransitionEvents
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ initialResults ->

            transitionCommand.invoke(initialResults)
            subscriber = null

        }, { error ->

            Log.e("Error", "Error fetching initial results", error)
        })
    }

    private val initialResults = this.searchService.citySearch()
}