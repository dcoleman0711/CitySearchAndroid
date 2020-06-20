package com.example.citysearch.unit

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.data.ImageSearchResult
import com.example.citysearch.data.ImageSearchResults
import com.example.citysearch.data.ImageSearchServiceImp
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.TestScheduler
import okhttp3.*
import okio.Okio
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL

@RunWith(AndroidJUnit4::class)
class ImageSearchServiceTests {

    lateinit var steps: ImageSearchServiceSteps

    val Given: ImageSearchServiceSteps get() = steps
    val When: ImageSearchServiceSteps get() = steps
    val Then: ImageSearchServiceSteps get() = steps

    @Before
    fun setUp() {
        
        steps = ImageSearchServiceSteps(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testImageSearchRequest() {

        val query = Given.query()
        val url = Given.searchUrl(query)
        val httpClient = Given.httpClient()
        val searchService = Given.searchService(httpClient)

        When.perform(searchService, query)

        Then.searchServiceRequestedURL(searchService, url)
    }

    @Test
    fun testImageSearchResponse() {

        val query = Given.query()
        val httpResponse = Given.httpResponse()
        val httpClient = Given.httpClient()
        Given.httpClientReturnsResponse(httpClient, httpResponse)
        val expectedResults = Given.expectedResults()
        val searchService = Given.searchService(httpClient)

        val results = When.perform(searchService, query)

        Then.resultsAreEqualTo(results, expectedResults)
    }
}

class ImageSearchServiceSteps(private val context: Context) {

    private var isOnQueue = false
    private var executedOnQueue = false

    private var requestedURL: URL? = null

    private var receivedResult: ImageSearchResults? = null

    private val httpClient = mock<OkHttpClient> {  }

    private val queue = TestScheduler()

    init {

        val emptyResponse = "{}"
        val emptyStream = ByteArrayInputStream(emptyResponse.toByteArray())

        val source = Okio.source(emptyStream)
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

        val call = mock<Call> {

            on { execute() }.thenReturn(response)
        }

        whenever(httpClient.newCall(any())).then { invocation ->

            val callRequest = invocation.getArgument<Request>(0)
            requestedURL = callRequest.url().url()

            call
        }
    }

    fun query(): String {

        return "searchQuery"
    }

    fun searchUrl(query: String): URL {

        // This isn't foolproof, because the query parameters can be reordered and it is still the "same"
        return URL("https://serpapi.com/search?api_key=7a28f042f0a5f2a8d3444fd64a074bceee47f5837f7ee953ee255ddc5640de3b&tbm=isch&ijn=0&q=searchQuery")
    }

    fun httpResponse(): InputStream {

        return context.assets.open("stubImageResponse.json")
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

    fun searchService(httpClient: OkHttpClient): ImageSearchServiceImp {

        return ImageSearchServiceImp(httpClient, queue)
    }

    fun expectedResults(): ImageSearchResults {

        return ImageSearchResults(arrayListOf(
            ImageSearchResult("https://www.apple.com/ac/structured-data/images/open_graph_logo.png?202005131207"),
            ImageSearchResult("https://as-images.apple.com/is/og-default?wid=1200&hei=630&fmt=jpeg&qlt=95&op_usm=0.5,0.5&.v=1525370171638"),
            ImageSearchResult("https://yt3.ggpht.com/a/AATXAJxK3dHVZIVCtxjYZ7mp77wBbCs9fw4zU46V_Q=s900-c-k-c0xffffffff-no-rj-mo"),
            ImageSearchResult("https://pbs.twimg.com/profile_images/1110319067280269312/iEqpsbUA_400x400.png"),
            ImageSearchResult("https://www.apple.com/ac/structured-data/images/knowledge_graph_logo.png?201809210816")
        ))
    }

    fun perform(searchService: ImageSearchServiceImp, query: String): ImageSearchResults? {

        val result = searchService.imageSearch(query)
        val subscriber = result.subscribe { results ->

            receivedResult = results
        }

        isOnQueue = true
        queue.triggerActions()
        isOnQueue = false

        return receivedResult!!
    }

    fun searchServiceRequestedURL(searchService: ImageSearchServiceImp, expectedURL: URL) {

        Assert.assertEquals("Request URL is not correct", expectedURL.toURI(), requestedURL?.toURI())
    }

    fun resultsAreEqualTo(results: ImageSearchResults?, expectedResults: ImageSearchResults) {

        Assert.assertEquals("Results are not the expected results", expectedResults, results)
    }
}