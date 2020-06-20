package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.details.imagecarousel.ImageCarouselModelImp
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModel
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModelFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.disposables.Disposable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@RunWith(AndroidJUnit4::class)
class ImageCarouselModelTests {

    lateinit var steps: ImageCarouselModelSteps

    val Given: ImageCarouselModelSteps get() = steps
    val When: ImageCarouselModelSteps get() = steps
    val Then: ImageCarouselModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = ImageCarouselModelSteps()
    }

    @Test
    fun testSetResults() {

        val imageURLs = Given.imageURLs()
        val expectedResultModels = Given.models(imageURLs)
        val imageCarouselModel = Given.imageCarouselModelCreated()
        val observer = Given.observeSearchResults(imageCarouselModel)

        When.imageCarouselModelDataIsSetToURLs(imageCarouselModel, imageURLs)

        Then.observerIsNotifiedWithValue(expectedResultModels)
    }
}

class ImageCarouselModelSteps {

    private val imageModelFactory = mock<AsyncImageModelFactory> {  }

    private var valuePassedToObserver: List<AsyncImageModel>? = null

    private var listenerIsNotifiedImmediately = false

    init {

        var modelsMap = HashMap<URL, AsyncImageModel>()
        whenever(imageModelFactory.imageModel(any())).then { invocation ->

            val url = invocation.getArgument<URL>(0)
            val model = modelsMap[url] ?: {

                val result = mock<AsyncImageModel>()
                modelsMap[url] = result
                result
            }()

            model
        }
    }

    fun imageURLs(): List<URL> {

        return (0 until 5).map({ index -> URL("https://images.com/image$index") })
    }

    fun models(imageURLs: List<URL>): List<AsyncImageModel> {

        return imageURLs.map( { url -> imageModelFactory.imageModel(url) } )
    }

    fun imageCarouselModelCreated(): ImageCarouselModelImp {

        return ImageCarouselModelImp(imageModelFactory)
    }

    fun imageCarouselModelDataIsSetToURLs(imageCarouselsModel: ImageCarouselModelImp, imageURLs: List<URL>) {

        imageCarouselsModel.setResults(imageURLs)
    }

    fun observeSearchResults(model: ImageCarouselModelImp): Disposable {

        return model.resultsModels.subscribe { resultsModels ->

            valuePassedToObserver = resultsModels
        }
    }

    fun observerIsNotifiedWithValue(expectedResults: List<AsyncImageModel>) {

        Assert.assertTrue("Observer was not notified with correct results", ListCompareUtility.compareListsWithPointerEquality(expectedResults, valuePassedToObserver))
    }
}

class ListCompareUtility {

    companion object {

        fun<T> compareListsWithPointerEquality(expected: List<T>?, actual: List<T>?): Boolean {

            return compareLists(expected, actual, { first, second -> first === second })
        }

        fun<T> compareLists(expected: List<T>?, actual: List<T>?, compare: (first: T, second: T) -> Boolean): Boolean {

            val expected = expected
            if(expected == null) {
                Assert.assertNull("Lists are not equal", actual)
                return false
            }

            val actual = actual
            if(actual == null) {
                return false
            }

            if(actual.size != actual.size) {
                return false
            }

            return (0 until expected.size).toList().stream().allMatch { index -> compare(actual[index], expected[index]) }
        }
    }
}