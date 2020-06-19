package com.example.citysearch.startup

import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.Window
import com.example.citysearch.data.*
import com.example.citysearch.reactive.ObservableFactory
import com.example.citysearch.reactive.ObservableFactoryImp
import io.reactivex.Observable
import io.reactivex.Observable.zip
import io.reactivex.Scheduler
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

class StartupModelImp(private val transitionCommand: StartupTransitionCommand,
                      private val searchService: CitySearchService,
                      private val observableFactory: ObservableFactory,
                      private val workScheduler: Scheduler,
                      private val invocationScheduler: Scheduler): StartupModel {

    constructor(transitionCommand: StartupTransitionCommand, searchService: CitySearchService) : this(
        transitionCommand,
        searchService, ObservableFactoryImp(),
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    override val appTitle = Observable.just("City Search")

    override fun startTransitionTimer() {

        val timer = observableFactory.timer(4, TimeUnit.SECONDS, workScheduler)

        val beginTransitionEvents = zip(initialResults, timer, BiFunction<CitySearchResults, Long, CitySearchResults> { initialResults, time -> initialResults })

        var subscriber: Disposable?
        subscriber = beginTransitionEvents
            .subscribeOn(workScheduler)
            .observeOn(invocationScheduler)
            .subscribe({ initialResults ->

            transitionCommand.invoke(initialResults)
            subscriber = null

        }, { error ->

            Log.e("Error", "Error fetching initial results", error)
        })
    }

    private val initialResults = this.searchService.citySearch()
}