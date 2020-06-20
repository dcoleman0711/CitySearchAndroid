package com.example.citysearch.parallax

import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

data class ParallaxLayer(val distance: Float, val image: Bitmap)

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