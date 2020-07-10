package com.example.citysearch.models

import android.util.Log
import com.example.citysearch.factories.ObservableFactory
import com.example.citysearch.factories.ObservableFactoryImp
import com.example.citysearch.commands.StartupTransitionCommand
import com.example.citysearch.entities.CitySearchResults
import com.example.citysearch.services.CitySearchService
import io.reactivex.Observable
import io.reactivex.Observable.zip
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Model for Startup MVVM.
 *
 * It fetches the initial city search results, and ensures the startup screen remains visible for at least 4 seconds.  Also exposes the text to display.
 */
interface StartupModel {

    val appTitle: Observable<String>

    fun startTransitionTimer()
}

class StartupModelImp(private val transitionCommand: StartupTransitionCommand,
                      private val searchService: CitySearchService,
                      private val observableFactory: ObservableFactory,
                      private val workScheduler: Scheduler,
                      private val invocationScheduler: Scheduler):
    StartupModel {

    constructor(transitionCommand: StartupTransitionCommand, searchService: CitySearchService) : this(
        transitionCommand,
        searchService, ObservableFactoryImp(),
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    override val appTitle = Observable.just("City Search")

    private var subscriber: Disposable? = null

    override fun startTransitionTimer() {

        val timer = observableFactory.timer(4, TimeUnit.SECONDS, workScheduler)

        val beginTransitionEvents = zip(initialResults, timer, BiFunction<CitySearchResults, Long, CitySearchResults> { initialResults, _ -> initialResults })

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