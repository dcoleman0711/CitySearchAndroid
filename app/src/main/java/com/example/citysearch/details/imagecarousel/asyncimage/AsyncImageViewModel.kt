package com.example.citysearch.details.imagecarousel.asyncimage

import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

interface AsyncImageViewModel {

    val image: Observable<Optional<Bitmap>>
}

class AsyncImageViewModelImp(private val model: AsyncImageModel): AsyncImageViewModel {

    override val image: Observable<Optional<Bitmap>>

    init {

        image = model.image.map { image -> Optional.of(image) }.observeOn(AndroidSchedulers.mainThread())
    }
}