package com.example.citysearch.data

import com.example.citysearch.startup.StartupFragment
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
import retrofit2.http.Headers
import retrofit2.http.Query
import java.io.InputStreamReader
import java.util.*

typealias CitySearchFuture = Observable<CitySearchResults>

interface CitySearchService {

    fun citySearch(): CitySearchFuture
}

class CitySearchServiceImp(private val client: OkHttpClient,
                           private val queue: Scheduler): CitySearchService {

    constructor() : this(OkHttpClient.Builder().build(), Schedulers.io())

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

        val start = 4000
        val count = 80

        return api.citySearch(start, count)
            .subscribeOn(queue)
    }
}

class CitySearchServiceStub(): CitySearchService {

    override fun citySearch(): CitySearchFuture {

        val stubFile = StartupFragment.context.assets.open("stubCityResponse.json")
        val stubFileContents = Gson().fromJson(InputStreamReader(stubFile), CitySearchResults::class.java)

        val stubResults = CitySearchResults(Collections.nCopies(5, stubFileContents.results ).flatten())

        return Observable.just(stubResults)
            .subscribeOn(Schedulers.io())
    }
}