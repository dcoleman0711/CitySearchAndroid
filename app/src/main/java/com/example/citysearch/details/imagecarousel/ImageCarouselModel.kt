package com.example.citysearch.details.imagecarousel

import android.content.Context
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModel
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModelFactory
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModelFactoryImp
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.net.URL

// Model for ImageCarousel MVVM.  Exposes a configurable stream of image URL lists, and handles converting a URL list into a list of cell models
interface ImageCarouselModel {

    val resultsModels: Observable<List<AsyncImageModel>>

    var results: Observable<List<URL>>
}

class ImageCarouselModelImp(modelFactory: AsyncImageModelFactory): ImageCarouselModel {

    constructor(context: Context) : this(AsyncImageModelFactoryImp(context))

    override val resultsModels: Observable<List<AsyncImageModel>>

    override var results = Observable.empty<List<URL>>()
    set(value) {

        field = value
        resultsStreams.onNext(value)
    }

    private val resultsStreams = BehaviorSubject.create<Observable<List<URL>>>()

    init {

        resultsModels = resultsStreams.switchMap { resultsStream ->
            resultsStream.map { results ->
                results.map { url ->
                    modelFactory.imageModel(url)
                }
            }
        }
    }
}