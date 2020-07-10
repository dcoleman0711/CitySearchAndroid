package com.example.citysearch.unit

import android.content.Context
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.services.ImageService
import com.example.citysearch.models.AsyncImageModelImp
import com.example.citysearch.utilities.ImageLoader
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
import java.net.URL

@RunWith(AndroidJUnit4::class)
class AsyncImageModelTests {

    lateinit var steps: AsyncImageModelSteps

    val Given: AsyncImageModelSteps get() = steps
    val When: AsyncImageModelSteps get() = steps
    val Then: AsyncImageModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = AsyncImageModelSteps(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testObserveImageImmediate() {

        val imageService = Given.imageService()
        val imageURL = Given.imageURL()
        val imageModel = Given.imageModelIsCreated(imageURL = imageURL, imageService = imageService)
        val image = Given.image()
        val observer = Given.imageServiceReturnsImageForURL(imageService, image, imageURL)

        When.observeSearchResults(imageModel)

        Then.observerIsNotifiedOfNewValue(image)
    }

    @Test
    fun testObserveImageUpdate() {

        val imageService = Given.imageService()
        val imageURL = Given.imageURL()
        val image = Given.image()
        val imageModel = Given.imageModelIsCreated(imageURL = imageURL, imageService = imageService)
        val observer = Given.observeSearchResults(imageModel)

        When.imageServiceReturnsImageForURL(imageService, image, imageURL)

        Then.observerIsNotifiedOfNewValue(image)
    }

    @Test
    fun testObserveImageFailure() {

        val imageService = Given.imageService()
        val imageURL = Given.imageURL()
        val missingImage = Given.missingImage()
        val imageModel = Given.imageModelIsCreated(imageURL = imageURL, imageService = imageService)
        val observer = Given.observeSearchResults(imageModel)

        When.imageServiceFailsForURL(imageService, imageURL)

        Then.observerIsNotifiedOfNewValue(missingImage)
    }
}

class AsyncImageModelSteps(private val context: Context) {

    private var valuePassedToObserver: Bitmap? = null

    private var imageFutures = HashMap<URL, BehaviorSubject<Bitmap>>()

    private val imageService = mock<ImageService> {

        on { fetchImage(any()) }.then { invocation ->

            val url = invocation.getArgument<URL>(0)

            val imageFuture = BehaviorSubject.create<Bitmap>()
            imageFutures[url] = imageFuture

            imageFuture
        }
    }

    fun imageService(): ImageService {

        return imageService
    }

    fun imageURL(): URL {

        return URL("https://")
    }

    fun image(): Bitmap {

        return ImageLoader.loadImage(context,"TestImage.jpg")
    }

    fun missingImage(): Bitmap {

        return ImageLoader.loadImage(context,"MissingImage.jpg")
    }

    fun imageServiceReturnsImageForURL(imageService: ImageService, image: Bitmap, url: URL) {

        val future = imageFutures[url]
        if(future == null)
            return

        future.onNext(image)
    }

    fun imageServiceFailsForURL(imageService: ImageService, url: URL) {

        val future = imageFutures[url]
        if(future == null)
            return

        future.onError(Exception("Test Exception"))
    }

    fun imageModelIsCreated(imageURL: URL, imageService: ImageService): AsyncImageModelImp {

        return AsyncImageModelImp(
            context,
            imageURL,
            imageService
        )
    }

    fun observeSearchResults(model: AsyncImageModelImp): Disposable {

        return model.image.subscribe { image ->

            valuePassedToObserver = image
        }
    }

    fun observerIsNotifiedOfNewValue(newValue: Bitmap) {

        Assert.assertTrue("Observer was not notified of correct results", newValue.sameAs(valuePassedToObserver))
    }
}