package com.example.citysearch.reactive

import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

interface ObservableFactory {

    fun timer(delay: Long, unit: TimeUnit, scheduler: Scheduler): Observable<Long>
}

class ObservableFactoryImp: ObservableFactory {

    override fun timer(delay: Long, unit: TimeUnit, scheduler: Scheduler): Observable<Long> {

        return Observable.timer(delay, unit, scheduler)
    }
}