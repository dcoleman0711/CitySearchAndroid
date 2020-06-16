package com.example.citysearch.parallax

import android.graphics.Bitmap
import com.example.citysearch.utilities.Point
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

interface ParallaxViewModel {

    val images: Observable<Array<Bitmap>>
    val offsets: Observable<Array<Point>>

    var contentOffset: Observable<Point>?
}

class ParallaxViewModelImp(private val model: ParallaxModel): ParallaxViewModel {

    override val images: Observable<Array<Bitmap>>
    override val offsets: BehaviorSubject<Array<Point>>

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

        images = model.layers.map { layers -> layers.map { layer -> layer.image }.toTypedArray() }.observeOn(AndroidSchedulers.mainThread())
        offsets = BehaviorSubject.create()
    }

    private fun createOffsetsPipe(contentOffset: Observable<Point>) {

        // We subscribe and publish to a subject instead of just assigning the offsets observable directly, because changing the offsets observable (part of the public interface of this ViewModel class) would mean the subscribers to it are subscribed to an obsolete observable.  This way, subscribers can stay subscribed to a permanent observable and always receive the up-to-date event stream
        val offsetsEvents: Observable<Array<Point>> = Observable.combineLatest(model.layers, contentOffset, BiFunction({ layers, offset -> offsets(layers, offset) }))

        offsetsBinding = offsetsEvents
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ offsets -> this.offsets.onNext(offsets) })
    }

    private fun offsets(layers: Array<ParallaxLayer>, offset: Point): Array<Point> {

        return layers.map { layer -> Point((-offset.x.toFloat() / layer.distance).toInt(), (-offset.y.toFloat() / layer.distance).toInt()) }.toTypedArray()
    }
}