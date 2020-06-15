package com.example.citysearch.data

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

typealias CitySearchFuture = Observable<CitySearchResults>

interface CitySearchService {

    fun citySearch(): CitySearchFuture
}

class CitySearchServiceImp: CitySearchService {

    companion object {

        private const val appIdKey = "X-Parse-Application-Id"
        private const val appId = "ADBPbkWUGMrBs09ckVscJ6lIduElFntjprkWY3Rn"

        private const val appKeyKey = "X-Parse-REST-API-Key"
        private const val appKey = "qxzeBZAV2pzUPlnkOJLjGainEkHCJVEdTnerWyTM"

        private const val baseURL = "https://parseapi.back4app.com"
        private const val path = "/classes/Continentscountriescities_City"
    }

    interface CitySearchApi {

        @Headers(
            "$appIdKey: $appId",
            "$appKeyKey: $appKey"
        )
        @GET(path)
        fun citySearch(@Query("skip") start: Int, @Query("limit") count: Int): CitySearchFuture
    }

    private val api: CitySearchApi

    init {

        val client = OkHttpClient.Builder().build()
        val parser = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(parser))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        api = retrofit.create(CitySearchApi::class.java)
    }

    override fun citySearch(): CitySearchFuture {

        val stubResultsArray: Array<CitySearchResult> = IntRange(0, 9).map { index -> CitySearchResult("Test $index", 15000, GeoPoint(0.0, 10.0)) }.toTypedArray()
        val stubResults = CitySearchResults(stubResultsArray)

        return Observable.just(stubResults)
            .subscribeOn(Schedulers.io())
//        val start = 4000
//        val count = 80
//
//        return api.citySearch(start, count)
//            .subscribeOn(Schedulers.io())
    }
}