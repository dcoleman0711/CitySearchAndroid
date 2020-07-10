package com.example.citysearch.data

import android.content.Context
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

/**
 * Interface for a City Search Service
 */
interface CitySearchService {

    fun citySearch(): CitySearchFuture
}

/**
 * Data class to specify a country filter to the city search results
 *
 * @property objectId The unique identifier in the database for the country
 */
data class CountryFilter(val objectId: String) {

    companion object {

        val unitedStates = CountryFilter("BXkZTl2omc")
    }

    val __type = "Pointer"
    val className = "Continentscountriescities_Country"
}

/**
 * Factory class for creating search filters.
 * Filter values are used to encode conditions like "greater than" or "less than" for search queries
 */
class FilterValue {

    companion object {

        fun greaterThan(value: Int): Map<String, Any> {

            return hashMapOf("\$gt" to value)
        }
    }
}

/**
 * Implementation of the search service that uses the REST web service.
 *
 * Uses OkHttp and Rx Schedulers
 */
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
        fun citySearch(@Query("skip") start: Int,
                       @Query("limit") count: Int,
                       @Query("where") filter: String,
                       @Query("order") orderBy: String): CitySearchFuture
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

        val start = 0
        val count = 80

        val countryFilter: Map<String, Any> = hashMapOf(
            "country" to CountryFilter.unitedStates,
            "population" to FilterValue.greaterThan(0)
        )

        val gson = Gson()
        val filter = gson.toJson(countryFilter)

        return api.citySearch(start, count, filter, "name")
            .subscribeOn(queue)
    }
}

/**
 * Stub service that returns locally stored results, useful for development
 */
class CitySearchServiceStub(private val context: Context): CitySearchService {

    override fun citySearch(): CitySearchFuture {

        val stubFile = context.assets.open("stubCityResponse.json")
        val stubFileContents = Gson().fromJson(InputStreamReader(stubFile), CitySearchResults::class.java)

        val stubResults = CitySearchResults(Collections.nCopies(5, stubFileContents.results ).flatten())

        return Observable.just(stubResults)
            .subscribeOn(Schedulers.io())
    }
}