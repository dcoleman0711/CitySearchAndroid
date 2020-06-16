package com.example.citysearch.parallax

import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

data class ParallaxLayer(val distance: Float, val image: Bitmap)

interface ParallaxModel {

    val layers: Observable<Array<ParallaxLayer>>

    fun setLayers(layers: Array<ParallaxLayer>)
}

class ParallaxModelImp: ParallaxModel {

    override val layers: BehaviorSubject<Array<ParallaxLayer>>

    init {

        layers = BehaviorSubject.create()
    }

    override fun setLayers(layers: Array<ParallaxLayer>) {

        this.layers.onNext(layers)
    }
}