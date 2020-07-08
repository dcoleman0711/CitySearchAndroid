package com.example.citysearch.unit

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.data.CitySearchServiceImp
import com.example.citysearch.data.GeoPoint
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.TestScheduler
import okhttp3.*
import okio.BufferedSource
import okio.Okio
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStream

@RunWith(AndroidJUnit4::class)
class CitySearchServiceTests {

    lateinit var steps: CitySearchServiceSteps

    val Given: CitySearchServiceSteps get() = steps
    val When: CitySearchServiceSteps get() = steps
    val Then: CitySearchServiceSteps get() = steps

    @Before
    fun setUp() {

        steps = CitySearchServiceSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testCitySearchResponse() {

        val httpResponse = Given.httpResponse()
        val urlSession = Given.httpClient()
        Given.httpClientReturnsResponse(urlSession, httpResponse)
        val expectedResults = Given.expectedResults()
        val searchService = Given.searchService(urlSession)

        val results = When.perform(searchService)

        Then.resultsAreEqualTo(results, expectedResults)
    }
}

class CitySearchServiceSteps(private val context: Context) {

    private var receivedResult: CitySearchResults? = null

    private val httpClient = mock<OkHttpClient> {  }

    private val queue = TestScheduler()

    private var isOnQueue = false
    private var executedOnQueue = false

    fun httpResponse(): InputStream {

        return context.assets.open("stubCityResponse.json")
    }

    fun httpClient(): OkHttpClient {

        return httpClient
    }

    fun httpClientReturnsResponse(httpClient: OkHttpClient, responseData: InputStream) {

        val source = Okio.source(responseData)
        val bufferedSource = Okio.buffer(source)

        val responseBody = mock<ResponseBody> {

            on { source() }.thenReturn(bufferedSource)
        }

        val request = Request.Builder()
            .url("https://testurl.com")
            .build()

        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("")
            .body(responseBody)
            .build()

        whenever(httpClient.newCall(any())).then { invocation ->

            val call = mock<Call> {

                on { execute() }.then {

                    executedOnQueue = isOnQueue
                    response
                }
            }

            call
        }
    }

    fun searchService(httpClient: OkHttpClient): CitySearchServiceImp {

        return CitySearchServiceImp(httpClient, queue)
    }

    fun expectedResults(): CitySearchResults {

        return CitySearchResults(arrayListOf(
            CitySearchResult("la Massana", 7211, GeoPoint(42.54499, 1.51483), "04"),
            CitySearchResult("El Tarter", 1052, GeoPoint(42.57952, 1.65362), "02"),
            CitySearchResult("Arinsal", 1419, GeoPoint(42.57205, 1.48453), "04"),
            CitySearchResult("les Escaldes", 15853, GeoPoint(42.50729, 1.53414), "08"),
            CitySearchResult("Canillo", 3292, GeoPoint(42.5676, 1.59756), "02"),
            CitySearchResult("Pas de la Casa", 2363, GeoPoint(42.54277, 1.73361), "03"),
            CitySearchResult("Andorra la Vella", 20430, GeoPoint(42.50779, 1.52109), "07"),
            CitySearchResult("Encamp", 11223, GeoPoint(42.53474, 1.58014), "03"),
            CitySearchResult("Ordino", 3066, GeoPoint(42.55623, 1.53319), "05"),
            CitySearchResult("Sant Julià de Lòria", 8022, GeoPoint(42.46372, 1.49129), "06")
        ))
    }

    fun perform(searchService: CitySearchServiceImp): CitySearchResults {

        val result = searchService.citySearch()
        result.subscribe { searchResults ->

            receivedResult = searchResults
        }

        isOnQueue = true
        queue.triggerActions()
        isOnQueue = false

        return receivedResult!!
    }

    fun resultsAreEqualTo(results: CitySearchResults, expectedResults: CitySearchResults) {

        Assert.assertTrue("Request was not executed on queue", executedOnQueue)
        Assert.assertEquals("Results are not the expected results", expectedResults, results)
    }
}