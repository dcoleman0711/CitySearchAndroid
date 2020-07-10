package com.example.citysearch.parallax

import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Data class describing a parallax layer, which includes the image to display, and how "far away" the layer should appear (which determines how quickly it scrolls by)
 *
 * @property distance The virtual distance of a layer, which determines how quickly it scrolls by (further away means slower scrolling)
 * @property image The image to display
 */
data class ParallaxLayer(val distance: Float, val image: Bitmap)

/**
 * Model for Parallax MVVM.  Exposes settable parallax layers
 */
interface ParallaxModel {

    val layers: Observable<List<ParallaxLayer>>

    fun setLayers(layers: List<ParallaxLayer>)
}

class ParallaxModelImp: ParallaxModel {

    override val layers: BehaviorSubject<List<ParallaxLayer>>

    init {

        layers = BehaviorSubject.create()
    }

    override fun setLayers(layers: List<ParallaxLayer>) {

        this.layers.onNext(layers)
    }
}