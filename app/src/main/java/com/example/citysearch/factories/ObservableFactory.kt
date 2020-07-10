package com.example.citysearch.factories

import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

/**
 * This makes Observable.timer mockable, to allow a unit test to substitute a manually firable Observable, and capture the provided delay
 */
interface ObservableFactory {

    fun timer(delay: Long, unit: TimeUnit, scheduler: Scheduler): Observable<Long>
}

class ObservableFactoryImp: ObservableFactory {

    override fun timer(delay: Long, unit: TimeUnit, scheduler: Scheduler): Observable<Long> {

        return Observable.timer(delay, unit, scheduler)
    }
}