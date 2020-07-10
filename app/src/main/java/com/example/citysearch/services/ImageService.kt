package com.example.citysearch.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL

typealias ImageFuture = Observable<Bitmap>

/**
 * Service for retrieving an image from a URL.
 */
interface ImageService {

    fun fetchImage(url: URL): ImageFuture
}

/**
 * Implementation for an image service that supports HTTP/HTTPS URLs to fetch and decode images from the web
 *
 * Uses OkHttp and RxSchedulers
 */
class ImageServiceImp(
    private val client: OkHttpClient,
    private val queue: Scheduler
): ImageService {

    constructor() : this(OkHttpClient.Builder().build(), Schedulers.io())

    override fun fetchImage(url: URL): ImageFuture {

        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)

        val responseFuture = Observable.fromCallable( { call.execute() })
            .subscribeOn(queue)

        return responseFuture.map { response -> parseResponse(response) }
            .share()
    }

    private fun parseResponse(response: Response): Bitmap {

        val inputStream = response.body()?.byteStream() ?: throw IOException("Cannot parse empty response")

        val bytes = inputStream.readBytes()
        val bufferedStream = ByteArrayInputStream(bytes)

        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeStream(bufferedStream, null, options)
        val width = options.outWidth
        val height = options.outHeight

        if(width > 2048 || height > 2048)
            throw IOException("Android will choke on this image!")

        bufferedStream.reset()

        return BitmapFactory.decodeStream(bufferedStream)
    }
}