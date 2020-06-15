package com.example.citysearch.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL

typealias ImageFuture = Observable<Bitmap>

interface ImageService {

    fun fetchImage(url: URL): ImageFuture
}

class ImageServiceImp: ImageService {

    override fun fetchImage(url: URL): ImageFuture {

        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)

        val responseFuture = Observable.fromCallable( { call.execute() })
            .subscribeOn(Schedulers.io())

        return responseFuture.map { response -> parseResponse(response) }
            .share()
    }

    private fun parseResponse(response: Response): Bitmap {

        val inputStream = response.body()?.byteStream()
        if(inputStream == null)
            throw IOException("Cannot parse empty response")

        return BitmapFactory.decodeStream(inputStream)
    }
}