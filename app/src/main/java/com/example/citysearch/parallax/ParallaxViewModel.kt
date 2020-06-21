package com.example.citysearch.parallax

import android.graphics.Bitmap
import com.example.citysearch.utilities.Point
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

interface ParallaxViewModel {

    val images: Observable<List<Bitmap>>
    val offsets: Observable<List<Point>>

    var contentOffset: Observable<Point>?
}

class ParallaxViewModelImp(private val model: ParallaxModel,
                           private val resultsQueue: Scheduler): ParallaxViewModel {

    constructor(model: ParallaxModel) : this(model, AndroidSchedulers.mainThread())

    override val images: Observable<List<Bitmap>>
    override val offsets: BehaviorSubject<List<Point>>

    private var offsetsBinding: Disposable? = null

    override var contentOffset: Observable<Point>? = null
        set(value) {

            field = value

            offsetsBinding = null

            if(value == null)
                return

            createOffsetsPipe(value)
        }

    init {

        images = model.layers.map { layers -> layers.map { layer -> layer.image } }.observeOn(resultsQueue)
        offsets = BehaviorSubject.create()
    }

    private fun createOffsetsPipe(contentOffset: Observable<Point>) {

        // We subscribe and publish to a subject instead of just assigning the offsets observable directly, because changing the offsets observable (part of the public interface of this ViewModel class) would mean the subscribers to it are subscribed to an obsolete observable.  This way, subscribers can stay subscribed to a permanent observable and always receive the up-to-date event stream
        val offsetsEvents: Observable<List<Point>> = Observable.combineLatest(model.layers, contentOffset, BiFunction({ layers, offset -> offsets(layers, offset) }))

        offsetsBinding = offsetsEvents
            .observeOn(resultsQueue)
            .subscribe({ offsets -> this.offsets.onNext(offsets) })
    }

    private fun offsets(layers: List<ParallaxLayer>, offset: Point): List<Point> {

        return layers.map { layer -> Point((-offset.x.toFloat() / layer.distance).toInt(), (-offset.y.toFloat() / layer.distance).toInt()) }
    }
}