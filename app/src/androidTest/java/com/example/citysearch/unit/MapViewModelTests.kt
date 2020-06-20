package com.example.citysearch.unit

import android.content.Context
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.data.GeoPoint
import com.example.citysearch.details.map.MapModel
import com.example.citysearch.details.map.MapViewModel
import com.example.citysearch.details.map.MapViewModelImp
import com.example.citysearch.utilities.ImageLoader
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.Rect
import com.example.citysearch.utilities.Size
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MapViewModelTests {

    lateinit var steps: MapViewModelSteps

    val Given: MapViewModelSteps get() = steps
    val When: MapViewModelSteps get() = steps
    val Then: MapViewModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = MapViewModelSteps(InstrumentationRegistry.getInstrumentation().context)
    }
    
    @Test
    fun testBackground() {

        val backgroundImage = Given.backgroundImage()
        val viewModel = Given.viewModelIsCreated()

        val imageObserver = When.observeBackground(viewModel)

        Then.observerReceivedImage(backgroundImage)
    }

    @Test
    fun testMarkerImage() {

        val markerImage = Given.markerImage()
        val viewModel = Given.viewModelIsCreated()

        val imageObserver = When.observeMarkerImage(viewModel)

        Then.observerReceivedImage(markerImage)
    }

    @Test
    fun testMarkerSize() {

        val markerSize = Given.markerSize()
        val geoCoordinates = Given.geoCoordinates()
        val model = Given.model()
        Given.modelUpdatedGeoCoordinatesTo(model, geoCoordinates)
        val viewModel = Given.viewModelIsCreated(model = model)

        val frameObserver = When.observeMarkerFrame(viewModel)

        Then.observerReceivedSize(markerSize)
    }

    @Test
    fun testMarkerPosition() {

        val geoCoordinates = Given.geoCoordinates()
        val markerPosition = Given.markerPosition(geoCoordinates)
        val model = Given.model()
        Given.modelUpdatedGeoCoordinatesTo(model, geoCoordinates)
        val viewModel = Given.viewModelIsCreated(model = model)

        val frameObserver = When.observeMarkerFrame(viewModel)

        Then.observerReceivedPosition(markerPosition)
    }
}

class MapViewModelSteps(private val context: Context) {

    private var observedImage: Optional<Bitmap>? = null
    private var observedFrame: Rect? = null
    
    private var updatedGeoCoordinates: GeoPoint? = null

    private val modelCoordinates = BehaviorSubject.create<GeoPoint>()
    private val model = mock<MapModel> {

        on { geoCoordinates }.thenReturn(modelCoordinates)
    }

    fun model(): MapModel {

        return model
    }

    fun backgroundImage(): Bitmap {

        return ImageLoader.loadImage(context, "MapBackground.jpg")
    }

    fun markerImage(): Bitmap {

        return ImageLoader.loadImage(context, "MapMarker.png")
    }

    fun markerSize(): Size {

        return Size(16, 16)
    }

    fun geoCoordinates(): GeoPoint {

        return GeoPoint(20.0, 130.0)
    }

    fun markerPosition(geoCoordinates: GeoPoint): Point {

        val xPercent = ((geoCoordinates.longitude / 360.0) + 0.5).rem(1.0)
        val yPercent = 1.0 - (geoCoordinates.latitude + 90.0) / 180.0

        return Point((xPercent * MapViewModel.resolution).toInt(), (yPercent * MapViewModel.resolution).toInt())
    }

    fun viewModelIsCreated(model: MapModel = this.model): MapViewModelImp {

        return MapViewModelImp(context, model)
    }

    fun observeBackground(viewModel: MapViewModelImp): Disposable {

        return viewModel.backgroundImage.subscribe { backgroundImage -> 
            
            observedImage = backgroundImage
        }
    }

    fun observeMarkerImage(viewModel: MapViewModelImp): Disposable {

        return viewModel.markerImage.subscribe { markerImage ->

            observedImage = markerImage
        }
    }

    fun observeMarkerFrame(viewModel: MapViewModelImp): Disposable {

        return viewModel.markerFrame.subscribe { markerFrame ->

            observedFrame = markerFrame
        }
    }

    fun modelUpdatedGeoCoordinatesTo(model: MapModel, geoCoordinates: GeoPoint) {

        modelCoordinates.onNext(geoCoordinates)
    }

    fun observerReceivedImage(expectedImage: Bitmap) {

        Assert.assertTrue("Marker Image Observer did not receive correct image", expectedImage.sameAs(observedImage?.get()))
    }

    fun observerReceivedSize(expectedSize: Size) {

        Assert.assertEquals("Marker Image Observer did not receive correct size", expectedSize, observedFrame?.size)
    }

    fun observerReceivedPosition(expectedPosition: Point) {

        Assert.assertEquals("Marker Image Observer did not receive correct position", expectedPosition, observedFrame?.origin)
    }
}