package com.example.citysearch.unit

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.data.ImageServiceImp
import com.example.citysearch.utilities.ImageLoader
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
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.URL

@RunWith(AndroidJUnit4::class)
class ImageServiceTests {

    lateinit var steps: ImageServiceSteps

    val Given: ImageServiceSteps get() = steps
    val When: ImageServiceSteps get() = steps
    val Then: ImageServiceSteps get() = steps

    @Before
    fun setUp() {
        
        steps = ImageServiceSteps()
    }

    @Test
    fun testImageFetchResponse() {

        val url = Given.url()
        val image = Given.image()
        val httpClient = Given.httpClient()
        Given.httpClientReturnsResponse(httpClient, image, url)
        val searchService = Given.searchService(httpClient)

        val results = When.perform(searchService, url)

        Then.resultIsEqualTo(results, image)
    }
}

class ImageServiceSteps {

    private var isOnQueue = false
    private var executedOnQueue = false

    private var receivedResult: Bitmap? = null

    private val emptyCall = mock<Call> {

        on { execute() }.then { invocation ->

            val emptyStream = ByteArrayInputStream(byteArrayOf())

            val source = Okio.source(emptyStream)
            val bufferedSource = Okio.buffer(source)

            val responseBody = mock<ResponseBody> {

                on { source() }.thenReturn(bufferedSource)
            }

            val request = Request.Builder()
                .url("https://testurl.com")
                .build()

            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("")
                .body(responseBody)
                .build()
        }
    }
    private val httpClient = mock<OkHttpClient> {  }

    private val queue = TestScheduler()

    fun url(): URL {

        return URL("https://images.com/testimage")
    }

    fun image(): Bitmap {

        return Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
    }

    fun httpClient(): OkHttpClient {

        return httpClient
    }

    fun httpClientReturnsResponse(httpClient: OkHttpClient, image: Bitmap, url: URL) {

        val outStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        val bytes = outStream.toByteArray()
        val inStream = ByteArrayInputStream(bytes)

        val source = Okio.source(inStream)
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

            val callRequest = invocation.getArgument<Request>(0)
            val callUrl = callRequest.url().url()

            if(callUrl != url)
                return@then emptyCall

            val call = mock<Call> {

                on { execute() }.then {

                    executedOnQueue = isOnQueue
                    response
                }
            }

            call
        }
    }

    fun searchService(httpClient: OkHttpClient): ImageServiceImp {

        return ImageServiceImp(httpClient, queue)
    }

    fun perform(searchService: ImageServiceImp, url: URL): Bitmap? {

        val result = searchService.fetchImage(url)
        val subscriber = result.subscribe { image ->

            receivedResult = image
        }

        isOnQueue = true
        queue.triggerActions()
        isOnQueue = false

        return receivedResult!!
    }

    fun resultIsEqualTo(result: Bitmap?, expectedResult: Bitmap) {

        Assert.assertTrue("Result is not the expected result", expectedResult.sameAs(result))
        Assert.assertTrue("Call was not executed on queue", executedOnQueue)
    }
}