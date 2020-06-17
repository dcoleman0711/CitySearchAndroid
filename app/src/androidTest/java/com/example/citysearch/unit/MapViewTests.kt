package com.example.citysearch.unit

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Placeholder
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.StubMeasureConverter
import com.example.citysearch.details.map.MapViewImp
import com.example.citysearch.details.map.MapViewModel
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.utilities.*
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MapViewTests {

    lateinit var steps: MapViewSteps

    val Given: MapViewSteps get() = steps
    val When: MapViewSteps get() = steps
    val Then: MapViewSteps get() = steps

    @Before
    fun setUp() {

        steps = MapViewSteps()
    }

    @Test
    fun testBackgroundImageViewData() {

        val viewModel = Given.viewModel()
        val backgroundImageView = Given.backgroundImageView()
        val binder = Given.binder()

        val mapView = When.mapViewIsCreated(backgroundImageView = backgroundImageView, viewModel = viewModel, binder = binder)

        Then.backgroundImageViewIsBoundToViewModel(backgroundImageView, viewModel)
    }

    @Test
    fun testBackgroundImageViewIsView() {

        val backgroundImageView = Given.backgroundImageView()

        val mapView = When.mapViewIsCreated(backgroundImageView = backgroundImageView)

        Then.viewIsChildOfMapView(backgroundImageView, mapView)
    }

    @Test
    fun testMarkerImageViewImage() {

        val viewModel = Given.viewModel()
        val markerImageView = Given.markerImageView()
        val binder = Given.binder()

        val mapView = When.mapViewIsCreated(markerImageView = markerImageView, viewModel = viewModel, binder = binder)

        Then.markerImageViewIsBoundToViewModel(markerImageView, viewModel)
    }

    @Test
    fun testMarkerImageViewFrame() {

        val frame = Given.frame()
        val viewModel = Given.viewModel()
        val markerImageView = Given.markerImageView()
        val mapView = Given.mapViewIsCreated(markerImageView = markerImageView, viewModel = viewModel)

        When.viewModelHasUpdatedFrame(viewModel, frame)

        Then.markerImageViewFrameIsCorrect(markerImageView, frame)
    }
}

class MapViewSteps {

    private val view = mock<ConstraintLayout> {  }
    private val backgroundImageView = mock<ImageView> {  }
    private val markerImageView = mock<ImageView> {  }
    private val placeholder = mock<Placeholder> {  }

    private val viewModelBackgroundImage = BehaviorSubject.create<Optional<Bitmap>>()
    private val viewModelMarkerImage = BehaviorSubject.create<Optional<Bitmap>>()
    private val viewModelMarkerFrame = BehaviorSubject.create<Rect>()

    private val viewModel = mock<MapViewModel> {

        on { backgroundImage }.thenReturn(viewModelBackgroundImage)
        on { markerImage }.thenReturn(viewModelMarkerImage)
        on { markerFrame }.thenReturn(viewModelMarkerFrame)
    }
    
    private val viewBinder = mock<ViewBinder> {  }

    private val constraints = mock<ConstraintSet> {  }

    private val constraintSetFactory = mock<ConstraintSetFactory> {

        on { constraintSet() }.thenReturn(constraints)
    }

    private val measureConverter = StubMeasureConverter.stubMeasureConverter()

    fun binder(): ViewBinder {

        return viewBinder
    }

    fun backgroundImageView(): ImageView {

        return backgroundImageView
    }

    fun markerImageView(): ImageView {

        return markerImageView
    }

    fun markerSize(): Size {

        return Size(16, 16)
    }

    fun markerPosition(): Point {

        return Point(64, 128)
    }

    fun frame(): Rect {

        return Rect(Point((0.2 * MapViewModel.resolution).toInt(), (0.4 * MapViewModel.resolution).toInt()), Size(16, 16))
    }

    fun viewModel(): MapViewModel {

        return viewModel
    }

    fun viewModelHasUpdatedFrame(viewModel: MapViewModel, frame: Rect) {

        viewModelMarkerFrame.onNext(frame)
    }

    fun mapViewIsCreated(backgroundImageView: ImageView = this.backgroundImageView, markerImageView: ImageView = this.markerImageView, viewModel: MapViewModel = this.viewModel, binder: ViewBinder = this.viewBinder): MapViewImp {

        return MapViewImp(view, backgroundImageView, markerImageView, placeholder, binder, viewModel, constraintSetFactory, measureConverter)
    }

    fun viewIsChildOfMapView(child: View, mapView: MapViewImp) {

        verify(view).addView(child)
    }

    fun backgroundImageViewIsBoundToViewModel(backgroundImageView: ImageView, viewModel: MapViewModel) {

        verify(viewBinder).bindImageView(backgroundImageView, viewModelBackgroundImage)
    }

    fun markerImageViewIsBoundToViewModel(markerImageView: ImageView, viewModel: MapViewModel) {

        verify(viewBinder).bindImageView(markerImageView, viewModelMarkerImage)
    }

    fun markerImageViewFrameIsCorrect(markerImageView: ImageView, expectedFrame: Rect) {

        verify(constraints, atLeastOnce()).constrainWidth(markerImageView.id, measureConverter.convertToPixels(expectedFrame.size.width))
        verify(constraints, atLeastOnce()).constrainHeight(markerImageView.id, measureConverter.convertToPixels(expectedFrame.size.height))

        verify(constraints, atLeastOnce()).constrainPercentWidth(placeholder.id, expectedFrame.origin.x.toFloat() / MapViewModel.resolution)
        verify(constraints, atLeastOnce()).constrainPercentHeight(placeholder.id, expectedFrame.origin.y.toFloat() / MapViewModel.resolution)
    }
}