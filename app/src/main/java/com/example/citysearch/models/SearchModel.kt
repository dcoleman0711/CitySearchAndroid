package com.example.citysearch.models

import android.content.Context
import com.example.citysearch.utilities.ImageLoader
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Model for Search MVVM.
 *
 * Constructs the parallax layers that are seen on the search screen and passes them to the parallax model
 */
interface SearchModel

class SearchModelImp(private val context: Context, private val parallaxModel: ParallaxModel, private val searchResultsModel: SearchResultsModel):
    SearchModel {

    private var subscription: Disposable?

    init {

        subscription = Observable.fromCallable { loadParallaxLayers() }
            .subscribeOn(Schedulers.io())
            .subscribe { layers ->

                parallaxModel.setLayers(layers)
                subscription = null
            }
    }

    private fun loadParallaxLayers(): List<ParallaxLayer> {

        return arrayListOf(
            ParallaxLayer(
                4.0f,
                ImageLoader.loadImage(context, "Parallax2.jpg")
            ),
            ParallaxLayer(
                2.0f,
                ImageLoader.loadImage(context, "Parallax1.png")
            )
        )
    }
}