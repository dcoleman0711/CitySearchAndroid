package com.example.citysearch.unit

import android.content.Context
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModel
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModelFactory
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModelImp
import com.example.citysearch.utilities.ImageLoader
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AsyncImageViewModelTests {

    lateinit var steps: AsyncImageViewModelSteps

    val Given: AsyncImageViewModelSteps get() = steps
    val When: AsyncImageViewModelSteps get() = steps
    val Then: AsyncImageViewModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = AsyncImageViewModelSteps(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testObserveImagesImmediate() {

        val model = Given.imageModel()
        val resultsQueue = Given.resultsQueue()
        val viewModel = Given.imageViewModelIsCreated(model = model, resultsQueue = resultsQueue)
        val image = Given.image()
        val observer = Given.modelUpdatedImageTo(model, image)

        When.observeImage(viewModel)

        Then.observerIsNotifiedWithValueOnResultsQueue(image, resultsQueue)
    }

    @Test
    fun testObserveImagesUpdate() {

        val model = Given.imageModel()
        val resultsQueue = Given.resultsQueue()
        val viewModel = Given.imageViewModelIsCreated(model = model, resultsQueue = resultsQueue)
        val image = Given.image()
        val observer = Given.observeImage(viewModel)

        When.modelUpdatedImageTo(model, image)

        Then.observerIsNotifiedWithValueOnResultsQueue(image, resultsQueue)
    }

    @Test
    fun testObserveImagesImmediatelyPublishNil() {

        val model = Given.imageModel()
        val resultsQueue = Given.resultsQueue()
        val viewModel = Given.imageViewModelIsCreated(model = model, resultsQueue = resultsQueue)

        val observer = When.observeImage(viewModel)

        Then.observerIsNotifiedWithNilOnResultsQueue(resultsQueue)
    }
}

class AsyncImageViewModelSteps(private val context: Context) {

    private val modelImage = BehaviorSubject.create<Bitmap>()
    private val asyncImageModel = mock<AsyncImageModel> {

        on { image }.thenReturn(modelImage)
    }

    private val resultViewModelFactory = mock<AsyncImageViewModelFactory> {  }

    private var valuePassedToObserver: Optional<Bitmap>? = null

    private var updatedImage: Bitmap? = null

    val resultsQueue = TestScheduler()
    private var isOnResultsQueue = false
    private var valuePassedOnResultsQueue = false

    fun imageModel(): AsyncImageModel {

        return asyncImageModel
    }

    fun modelUpdatedImageTo(model: AsyncImageModel, image: Bitmap) {

        modelImage.onNext(image)
    }

    fun image(): Bitmap {

        return ImageLoader.loadImage(context, "TestImage.jpg")
    }

    fun resultsQueue(): Scheduler {

        return resultsQueue
    }

    fun imageViewModelIsCreated(model: AsyncImageModel, resultsQueue: Scheduler = this.resultsQueue): AsyncImageViewModelImp {

        return AsyncImageViewModelImp(model, resultsQueue)
    }

    fun observeImage(viewModel: AsyncImageViewModelImp): Disposable {

        return viewModel.image.subscribe { image ->

            valuePassedToObserver = image
        }
    }

    fun observerIsNotifiedWithValueOnResultsQueue(expectedValue: Bitmap, resultsQueue: Scheduler) {

        checkNotifiedOnQueue(Optional.of(expectedValue))
    }

    fun observerIsNotifiedWithNilOnResultsQueue(resultsQueue: Scheduler) {

        checkNotifiedOnQueue(Optional.empty())
    }

    private fun checkNotifiedOnQueue(expectedResult: Optional<Bitmap>) {

        Assert.assertNull("Observer was not notified on queue", valuePassedToObserver)

        this.resultsQueue.triggerActions()

        val image = valuePassedToObserver
        if(image == null) {
            Assert.fail("Observer was not notified")
            return
        }

        if(expectedResult.isPresent) {

            Assert.assertTrue("Observer was not notified of correct results", image.get().sameAs(expectedResult.get()))

        } else {

            Assert.assertFalse("Observer was not notified of correct results", image.isPresent)
        }
    }
}