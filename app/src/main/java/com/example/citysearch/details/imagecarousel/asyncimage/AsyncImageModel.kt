package com.example.citysearch.details.imagecarousel.asyncimage

import android.content.Context
import android.graphics.Bitmap
import com.example.citysearch.data.ImageService
import com.example.citysearch.utilities.ImageLoader
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable
import java.net.URL

// Model for Async Image MVVM.  Exposes the state for the bitmap to be displayed, and uses an image service to convert a URL to an image
interface AsyncImageModel {

    val image: Observable<Bitmap>
}

class AsyncImageModelImp(context: Context, imageURL: URL, imageService: ImageService): AsyncImageModel {

    override val image: ConnectableObservable<Bitmap>

    init {

        image = imageService.fetchImage(imageURL).onErrorResumeNext({ error: Throwable ->
            Observable.just(missingImage)
        }).replay(1)

        image.connect()
    }

    private val missingImage: Bitmap = ImageLoader.loadImage(context, "MissingImage.jpg")
}