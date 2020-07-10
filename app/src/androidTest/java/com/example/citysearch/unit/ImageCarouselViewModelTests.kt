package com.example.citysearch.unit

import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.models.ImageCarouselModel
import com.example.citysearch.viewmodels.ImageCarouselViewModelImp
import com.example.citysearch.models.AsyncImageModel
import com.example.citysearch.viewmodels.AsyncImageViewModel
import com.example.citysearch.factories.AsyncImageViewModelFactory
import com.example.citysearch.viewmodels.CellData
import com.example.citysearch.viewmodels.RecyclerViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.collections.HashMap

@RunWith(AndroidJUnit4::class)
class ImageCarouselViewModelTests {

    lateinit var steps: ImageCarouselViewModelSteps

    val Given: ImageCarouselViewModelSteps get() = steps
    val When: ImageCarouselViewModelSteps get() = steps
    val Then: ImageCarouselViewModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = ImageCarouselViewModelSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testObserveSearchResultsImmediate() {

        val model = Given.imageCarouselModel()
        val resultsQueue = Given.resultsQueue()
        val viewModel = Given.imageCarouselViewModelIsCreated(model = model, resultsQueue = resultsQueue)
        val resultModels = Given.resultModels()
        val resultViewModels = Given.resultViewModels(resultModels)
        val resultsData = Given.resultsData(resultViewModels)
        val observer = Given.modelUpdatesResultsToModels(resultModels)

        When.observeSearchResults(viewModel)

        Then.observerIsNotifiedWithResults(resultsData)
    }

    @Test
    fun testObserveSearchResultsUpdate() {

        val model = Given.imageCarouselModel()
        val resultsQueue = Given.resultsQueue()
        val viewModel = Given.imageCarouselViewModelIsCreated(model = model, resultsQueue = resultsQueue)
        val resultModels = Given.resultModels()
        val resultViewModels = Given.resultViewModels(resultModels)
        val resultsData = Given.resultsData(resultViewModels)
        val observer = Given.observeSearchResults(viewModel)

        When.modelUpdatesResultsToModels(resultModels)

        Then.observerIsNotifiedWithResultsOnQueue(resultsData, resultsQueue)
    }
}

class ImageCarouselViewModelSteps(private val context: Context) {

    private val resultViewModelFactory = mock<AsyncImageViewModelFactory> { }

    private val modelResults = BehaviorSubject.create<List<AsyncImageModel>>()
    private val model = mock<ImageCarouselModel> {

        on { resultsModels }.thenReturn(modelResults)
    }

    private val workQueue = TestScheduler()
    private val resultsQueue = TestScheduler()

    private var valuePassedToObserver: RecyclerViewModel<AsyncImageViewModel>? = null

    private var images = HashMap<Int, Bitmap>()
    private var viewModelModels = HashMap<Int, Int>()

    private var isOnResultsQueue = false
    private var valuePassedOnResultsQueue = false

    fun resultViewModels(resultModels: List<AsyncImageModel>): List<AsyncImageViewModel> {

        val viewModelsMap = HashMap<Int, AsyncImageViewModel>()

        whenever(resultViewModelFactory.viewModel(any())).then { invocation ->

            val model = invocation.getArgument<AsyncImageModel>(0)
            val modelID = System.identityHashCode(model)
            val viewModel = viewModelsMap[modelID] ?: {

                val result = mock<AsyncImageViewModel>()
                viewModelsMap[modelID] = result
                result
            }()

            val viewModelID = System.identityHashCode(viewModel)
            viewModelModels[viewModelID] = modelID

            viewModel
        }

        return resultModels.map( { resultModel -> resultViewModelFactory.viewModel(resultModel) } )
    }

    fun resultsQueue(): Scheduler {

        return resultsQueue
    }

    fun imageCarouselViewModelIsCreated(model: ImageCarouselModel, resultsQueue: Scheduler = this.resultsQueue): ImageCarouselViewModelImp {

        return ImageCarouselViewModelImp(
            model,
            resultViewModelFactory,
            workQueue,
            resultsQueue
        )
    }

    fun imageCarouselModel(): ImageCarouselModel {

        return model
    }

    fun modelUpdatesResultsToModels(resultModels: List<AsyncImageModel>) {

        modelResults.onNext(resultModels)
    }

    fun resultModels(): List<AsyncImageModel> {

        return (0 until 5).map {

            val model = mock<AsyncImageModel>()

            val modelID = System.identityHashCode(model)
            val image: Bitmap = images[modelID] ?: {

                val aspectRatio = 1.5
                val imageHeight = 64.0
                val size = Size((aspectRatio * imageHeight).toInt(), imageHeight.toInt())

                val image = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
                images[modelID] = image

                image
            }()

            whenever(model.image).thenReturn(Observable.just(image))
            model
        }
    }

    fun resultsData(viewModels: List<AsyncImageViewModel>): RecyclerViewModel<AsyncImageViewModel> {

        val cells = viewModels.map { viewModel ->

            val modelID = viewModelModels[System.identityHashCode(viewModel)]!!
            val image = images[modelID]!!
            val aspectRatio = image.width.toFloat() / image.height.toFloat()
            val cellHeight =  256.0f
            val cellSize = Size((aspectRatio * cellHeight).toInt(), cellHeight.toInt())

            CellData(viewModel, cellSize, null)
        }

        return RecyclerViewModel(cells, 0, 0)
    }

    fun observeSearchResults(viewModel: ImageCarouselViewModelImp): Disposable {

        return viewModel.results.subscribe { results ->

            valuePassedToObserver = results
            valuePassedOnResultsQueue = isOnResultsQueue
        }
    }

    fun observerIsNotifiedWithResults(expectedResults: RecyclerViewModel<AsyncImageViewModel>) {

        workQueue.triggerActions()

        isOnResultsQueue = true
        resultsQueue.triggerActions()
        isOnResultsQueue = false

        Assert.assertTrue("Observer was not notified with correct results", ListCompareUtility.compareLists(expectedResults.cells, valuePassedToObserver?.cells, { first, second -> first.viewModel === second.viewModel && first.size == second.size }))
    }

    fun observerIsNotifiedWithResultsOnQueue(expectedResults: RecyclerViewModel<AsyncImageViewModel>, resultsQueue: Scheduler) {

        this.observerIsNotifiedWithResults(expectedResults)
        Assert.assertTrue("Observer was not notified on results queue", valuePassedOnResultsQueue)
    }
}