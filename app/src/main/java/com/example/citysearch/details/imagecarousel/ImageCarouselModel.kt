package com.example.citysearch.details.imagecarousel

import android.content.Context
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModel
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModelFactory
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModelFactoryImp
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.net.URL

interface ImageCarouselModel {

    val resultsModels: Observable<Array<AsyncImageModel>>

    fun setResults(results: Array<URL>)
}

class ImageCarouselModelImp(modelFactory: AsyncImageModelFactory): ImageCarouselModel {

    constructor(context: Context) : this(AsyncImageModelFactoryImp(context))

    override val resultsModels: Observable<Array<AsyncImageModel>>

    private val results = BehaviorSubject.create<Array<URL>>()

    init {

        resultsModels = results.map { results -> results.map { url -> modelFactory.imageModel(url) }.toTypedArray() }
    }

    override fun setResults(results: Array<URL>) {

        this.results.onNext(results)
    }
}