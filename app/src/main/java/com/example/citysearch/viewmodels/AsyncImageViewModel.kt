package com.example.citysearch.viewmodels

import android.graphics.Bitmap
import com.example.citysearch.models.AsyncImageModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * ViewModel for AsyncImage MVVM.
 *
 * Primarily ensures that the bitmap to show is delivered on the main thread
 */
interface AsyncImageViewModel {

    val image: Observable<Optional<Bitmap>>
}

class AsyncImageViewModelImp(private val model: AsyncImageModel, private val resultsQueue: Scheduler):
    AsyncImageViewModel {

    constructor(model: AsyncImageModel) : this(model, AndroidSchedulers.mainThread())

    override val image: Observable<Optional<Bitmap>>

    init {

        image = Observable.just(Optional.empty<Bitmap>())
            .mergeWith(model.image.map { image -> Optional.of(image) })
            .observeOn(resultsQueue)
    }
}