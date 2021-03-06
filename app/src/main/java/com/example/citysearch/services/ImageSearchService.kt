package com.example.citysearch.services

import android.content.Context
import com.example.citysearch.entities.ImageSearchResults
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.InputStreamReader

typealias ImageSearchFuture = Observable<ImageSearchResults>

/**
 * Interface for an image search service, which provides a service to do an image search with a text query
 */
interface ImageSearchService {

    fun imageSearch(query: String): ImageSearchFuture
}

/**
 * Implementation of an image search service using the REST web service
 *
 * Uses OkHttp and Rx Schedulers
 */
class ImageSearchServiceImp(
    private val client: OkHttpClient,
    private val queue: Scheduler
):
    ImageSearchService {

    constructor() : this(OkHttpClient.Builder().build(), Schedulers.io())

    companion object {

        private const val appKey = "7a28f042f0a5f2a8d3444fd64a074bceee47f5837f7ee953ee255ddc5640de3b"

        private const val baseURL = "https://serpapi.com"
        private const val path = "/search"
    }

    interface ImageSearchApi {

        // I wish @QueryParam was still a thing...
        @GET("$path?api_key=$appKey&tbm=isch&ijn=0")
        fun imageSearch(@Query("q") query: String): ImageSearchFuture
    }

    private val api: ImageSearchApi

    init {

        val parser = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(parser))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        api = retrofit.create(ImageSearchApi::class.java)
    }

    override fun imageSearch(query: String): ImageSearchFuture {

        return api.imageSearch(query)
            .subscribeOn(queue)
    }
}

/**
 * Stub service that returns locally stored results, useful for development
 */
class ImageSearchServiceStub(private val context: Context):
    ImageSearchService {

    override fun imageSearch(query: String): ImageSearchFuture {

        val stubFile = context.assets.open("stubImageResponse.json")
        val stubResults = Gson().fromJson(InputStreamReader(stubFile), ImageSearchResults::class.java)

        return Observable.just(stubResults)
            .subscribeOn(Schedulers.io())
    }
}