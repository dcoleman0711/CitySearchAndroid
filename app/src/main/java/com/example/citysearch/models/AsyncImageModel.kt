package com.example.citysearch.models

import android.content.Context
import android.graphics.Bitmap
import com.example.citysearch.services.ImageService
import com.example.citysearch.utilities.ImageLoader
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable
import java.net.URL

/**
 * Model for Async Image MVVM.
 *
 * Exposes the state for the bitmap to be displayed, and uses an image service to convert a URL to an image
 */
interface AsyncImageModel {

    val image: Observable<Bitmap>
}

class AsyncImageModelImp(
    context: Context, imageURL:
    URL, imageService: ImageService
): AsyncImageModel {

    override val image: ConnectableObservable<Bitmap>

    init {

        image = imageService.fetchImage(imageURL).onErrorResumeNext { _: Throwable ->
            Observable.just(missingImage)
        }.replay(1)

        image.connect()
    }

    private val missingImage: Bitmap = ImageLoader.loadImage(context, "MissingImage.jpg")
}