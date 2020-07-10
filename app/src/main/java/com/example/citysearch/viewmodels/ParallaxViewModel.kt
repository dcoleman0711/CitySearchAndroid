package com.example.citysearch.viewmodels

import android.graphics.Bitmap
import android.graphics.Point
import androidx.lifecycle.ViewModel
import com.example.citysearch.models.ParallaxLayer
import com.example.citysearch.models.ParallaxModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

/**
 * ViewModel for Parallax MVVM.
 *
 * Exposes settable content offset, and converts the model's parallax layers into offsets for the displayed images
 */
interface ParallaxViewModel {

    val images: Observable<List<Bitmap>>
    val offsets: Observable<List<Point>>

    var contentOffset: Observable<Point>?
}

class ParallaxViewModelImp(
    private val model: ParallaxModel,
    private val resultsQueue: Scheduler
): ParallaxViewModel, ViewModel() {

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

        images = model.layers
            .map { layers -> layers.map { layer -> layer.image } }.observeOn(resultsQueue)

        offsets = BehaviorSubject.create()
    }

    private fun createOffsetsPipe(contentOffset: Observable<Point>) {

        // We subscribe and publish to a subject instead of just assigning the offsets observable directly,
        // because changing the offsets observable (part of the public interface of this ViewModel class)
        // would mean the subscribers to it are subscribed to an obsolete observable.
        // This way, subscribers can stay subscribed to a permanent observable and always receive
        // the up-to-date event stream
        // Another way to do this (see the Image Carousel model for an example)
        // is to flat or switch-map an observable of observables

        // Note that splitting this out and making the type explicit is to allow the compiler
        // to deduce the generic types
        val offsetsEvents: Observable<List<Point>> = Observable
            .combineLatest(model.layers, contentOffset, BiFunction {
                    layers, offset -> offsets(layers, offset)
            })

        offsetsBinding = offsetsEvents
            .observeOn(resultsQueue)
            .subscribe({ offsets -> this.offsets.onNext(offsets) })
    }

    private fun offsets(layers: List<ParallaxLayer>, offset: Point): List<Point> {

        return layers.map { layer ->
            Point(
                (-offset.x.toFloat() / layer.distance).toInt(),
                (-offset.y.toFloat() / layer.distance).toInt()
            )
        }
    }
}