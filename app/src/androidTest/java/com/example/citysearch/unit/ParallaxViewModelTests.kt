package com.example.citysearch.unit

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.parallax.ParallaxLayer
import com.example.citysearch.parallax.ParallaxModel
import com.example.citysearch.parallax.ParallaxViewModelImp
import com.example.citysearch.utilities.Point
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParallaxViewModelTests {

    lateinit var steps: ParallaxViewModelSteps

    val Given: ParallaxViewModelSteps get() = steps
    val When: ParallaxViewModelSteps get() = steps
    val Then: ParallaxViewModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = ParallaxViewModelSteps()
    }

    @Test
    fun testOffsets() {

        val model = Given.model()
        val parallaxLayers = Given.parallaxLayers()
        Given.modelUpdatesLayers(model, parallaxLayers)
        val viewModel = Given.detailsViewModel(model = model)
        val offset = Given.offset()
        val expectedOffsets = Given.expectedOffsets(parallaxLayers, offset)
        val offsetsObserver = Given.observeOffsets(viewModel)

        When.detailsViewModelOffsetIsUpdatedTo(viewModel, offset)

        Then.offsetsAreUpdatedTo(expectedOffsets)
    }

    @Test
    fun testImages() {

        val model = Given.model()
        val parallaxLayers = Given.parallaxLayers()
        val viewModel = Given.detailsViewModel(model = model)
        val images = Given.images(parallaxLayers)
        val imagesObserver = Given.observeImages(viewModel)

        When.modelUpdatesLayers(model, parallaxLayers)

        Then.imagesAreUpdatedTo(images)
    }
}

class ParallaxViewModelSteps {
    
    private var updatedOffsets: List<Point>? = null
    private var updatedImages: List<Bitmap>? = null

    private val modelLayers = BehaviorSubject.create<List<ParallaxLayer>>()
    private val model = mock<ParallaxModel> {

        on { layers }.thenReturn(modelLayers)
    }

    private val resultsQueue = TestScheduler()

    fun model(): ParallaxModel {

        return model
    }

    fun parallaxLayers(): List<ParallaxLayer> {

        return (0 until 5).map { index -> ParallaxLayer(10.0f / (index + 1).toFloat(), Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888)) }
    }

    fun images(layers: List<ParallaxLayer>): List<Bitmap> {

        return layers.map { layer -> layer.image }
    }

    fun modelUpdatesLayers(model: ParallaxModel, parallaxLayers: List<ParallaxLayer>) {

        modelLayers.onNext(parallaxLayers)
    }

    fun detailsViewModel(model: ParallaxModel): ParallaxViewModelImp {

        return ParallaxViewModelImp(model, resultsQueue)
    }

    fun offset(): Point {

        return Point(80, 0)
    }

    fun expectedOffsets(layers: List<ParallaxLayer>, offset: Point): List<Point> {

        return layers.map { layer -> Point((-offset.x.toFloat() / layer.distance).toInt(), (-offset.y.toFloat() / layer.distance).toInt()) }
    }

    fun detailsViewModelOffsetIsUpdatedTo(viewModel: ParallaxViewModelImp, offset: Point) {

        viewModel.contentOffset = Observable.just(offset)
    }

    fun observeOffsets(viewModel: ParallaxViewModelImp): Disposable {

        return viewModel.offsets.subscribe { offsets ->
            
            updatedOffsets = offsets
        }
    }

    fun observeImages(viewModel: ParallaxViewModelImp): Disposable {

        return viewModel.images.subscribe { images ->

            updatedImages = images
        }
    }

    fun offsetsAreUpdatedTo(expectedOffsets: List<Point>) {

        resultsQueue.triggerActions()

        Assert.assertEquals("Offsets were not updated correctly", expectedOffsets, updatedOffsets)
    }

    fun imagesAreUpdatedTo(expectedImages: List<Bitmap>) {

        resultsQueue.triggerActions()
        
        Assert.assertEquals("Images were not updated correctly", expectedImages, updatedImages)
    }
}