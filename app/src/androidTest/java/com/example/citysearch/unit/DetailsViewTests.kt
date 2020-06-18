package com.example.citysearch.unit

import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.StubMeasureConverter
import com.example.citysearch.animations.ShimmeringLoaderView
import com.example.citysearch.details.CityDetailsView
import com.example.citysearch.details.CityDetailsViewModel
import com.example.citysearch.details.imagecarousel.ImageCarouselView
import com.example.citysearch.details.map.MapView
import com.example.citysearch.reactive.TextViewModel
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.utilities.ConstraintSetFactory
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsViewTests {

    lateinit var steps: DetailsViewSteps

    val Given: DetailsViewSteps get() = steps
    val When: DetailsViewSteps get() = steps
    val Then: DetailsViewSteps get() = steps

    @Before
    fun setUp() {
        
        steps = DetailsViewSteps()
    }

    @Test
    fun testTitlePositionConstraints() {

        val titleLabel = Given.titleLabel()
        val contentView = Given.contentView()
        val expectedConstraints = Given.titleLabelConstraintsToTopLeftSafeAreaOf(titleLabel, contentView)

        val detailsView = When.detailsViewIsCreated(contentView = contentView, titleLabel = titleLabel)

        Then.detailsViewHasExpectedConstraints(detailsView, expectedConstraints)
    }

    @Test
    fun testTitleBindToViewModel() {

        val titleLabel = Given.titleLabel()
        val viewModel = Given.viewModel()
        val binder = Given.binder()

        val detailsView = When.detailsViewIsCreated(titleLabel = titleLabel, viewModel = viewModel, binder = binder)

        Then.titleLabelIsBoundToViewModel(titleLabel, viewModel)
    }

    @Test
    fun testPopulationTitleLabelPositionConstraints() {

        val populationTitleLabel = Given.populationTitleLabel()
        val titleLabel = Given.titleLabel()
        val contentView = Given.contentView()
        val expectedConstraints = Given.populationTitleLabelConstraintsToLeftAlignAndSpaceBelow(populationTitleLabel, titleLabel)

        val detailsView = Given.detailsViewIsCreated(contentView = contentView, titleLabel = titleLabel, populationTitleLabel = populationTitleLabel)

        Then.detailsViewHasExpectedConstraints(detailsView, expectedConstraints)
    }

    @Test
    fun testPopulationTitleLabelBindToViewModel() {

        val populationTitleLabel = Given.populationTitleLabel()
        val viewModel = Given.viewModel()
        val binder = Given.binder()

        val detailsView = When.detailsViewIsCreated(populationTitleLabel = populationTitleLabel, viewModel = viewModel, binder = binder)

        Then.populationTitleLabelIsBoundToViewModel(populationTitleLabel, viewModel)
    }

    @Test
    fun testPopulationLabelPositionConstraints() {

        val populationLabel = Given.populationLabel()
        val populationTitleLabel = Given.populationTitleLabel()
        val contentView = Given.contentView()
        val expectedConstraints = Given.populationLabelConstraintsToRightAndVerticallyCenteredWith(populationLabel, populationTitleLabel)

        val detailsView = When.detailsViewIsCreated(contentView = contentView, populationTitleLabel = populationTitleLabel, populationLabel = populationLabel)

        Then.detailsViewHasExpectedConstraints(detailsView, expectedConstraints)
    }

    @Test
    fun testPopulationLabelBindToViewModel() {

        val populationLabel = Given.populationLabel()
        val viewModel = Given.viewModel()
        val binder = Given.binder()

        val detailsView = When.detailsViewIsCreated(populationLabel = populationLabel, viewModel = viewModel, binder = binder)

        Then.populationLabelIsBoundToViewModel(populationLabel, viewModel)
    }

    @Test
    fun testMapViewPositionConstraints() {

        val mapView = Given.mapView()
        val contentView = Given.contentView()
        val expectedConstraints = Given.mapViewConstraintsToTopRightWithHalfWidthAnd2to1AspectRatio(mapView, contentView)

        val detailsView = When.detailsViewIsCreated(contentView = contentView, mapView = mapView)

        Then.detailsViewHasExpectedConstraints(detailsView, expectedConstraints)
    }

    @Test
    fun testImageCarouselViewPositionConstraints() {

        val imageCarouselView = Given.imageCarouselView()
        val mapView = Given.mapView()
        val contentView = Given.contentView()
        val expectedConstraints = Given.imageCarouselViewConstraintsSafeAreaBottomAndEdgesOfAndSpacedBelowWithCorrectHeight(imageCarouselView, contentView, mapView)

        val detailsView = When.detailsViewIsCreated(contentView = contentView, mapView = mapView, imageCarouselView = imageCarouselView)

        Then.detailsViewHasExpectedConstraints(detailsView, expectedConstraints)
    }

    @Test
    fun testShimmeringLoaderViewPositionConstraints() {

        val shimmeringLoader = Given.shimmeringLoader()
        val imageCarouselView = Given.imageCarouselView()
        val expectedConstraints = Given.shimmeringLoaderConstrainedToMatchFrameOfImageCarousel(shimmeringLoader, imageCarouselView)

        val detailsView = When.detailsViewIsCreated(imageCarouselView = imageCarouselView, shimmeringLoader = shimmeringLoader)

        Then.detailsViewHasExpectedConstraints(detailsView, expectedConstraints)
    }

    @Test
    fun testShimmeringLoaderViewStartsAnimating() {

        val shimmeringLoader = Given.shimmeringLoader()
        val viewModel = Given.viewModel()
        val detailsView = Given.detailsViewIsCreated(shimmeringLoader = shimmeringLoader, viewModel = viewModel)

        When.viewModelPublishesShowLoader(viewModel)

        Then.shimmeringLoaderStartedAnimating(shimmeringLoader)
    }

    @Test
    fun testShimmeringLoaderViewStopsAnimating() {

        val shimmeringLoader = Given.shimmeringLoader()
        val viewModel = Given.viewModel()
        val detailsView = Given.detailsViewIsCreated(shimmeringLoader = shimmeringLoader, viewModel = viewModel)

        When.viewModelPublishesHideLoader(viewModel)

        Then.shimmeringLoaderStopppedAnimating(shimmeringLoader)
    }
}

class DetailsViewSteps {

    private val view = mock<ScrollView> {  }

    private val contentView = mock<ConstraintLayout> {  }

    private val titleLabel = mock<TextView> {  }
    private val populationTitleLabel = mock<TextView> {  }
    private val populationLabel = mock<TextView> {  }

    private val mapView = mock<MapView> {

        on { view }.thenReturn(mock())
    }

    private val imageCarouselView = mock<ImageCarouselView> {

        on { view }.thenReturn(mock())
    }

    private val shimmeringLoader = mock<ShimmeringLoaderView> {

        on { view }.thenReturn(mock())
    }

    private val viewModelTitle = BehaviorSubject.create<TextViewModel>()
    private val viewModelPopulationTitle = BehaviorSubject.create<TextViewModel>()
    private val viewModelPopulation = BehaviorSubject.create<TextViewModel>()
    private val viewModelShowLoader = BehaviorSubject.create<Boolean>()

    private val viewModel = mock<CityDetailsViewModel> {

        on { title }.thenReturn(viewModelTitle)
        on { populationTitle }.thenReturn(viewModelPopulationTitle)
        on { population }.thenReturn(viewModelPopulation)
        on { showLoader }.thenReturn(viewModelShowLoader)
    }

    private val viewBinder = mock<ViewBinder> {  }

    private val constraints = mock<ConstraintSet> {  }

    private val constraintSetFactory = mock<ConstraintSetFactory> {

        on { constraintSet() }.thenReturn(constraints)
    }

    private val measureConverter = StubMeasureConverter.stubMeasureConverter()
    
    fun contentView(): ConstraintLayout {

        return contentView
    }

    fun titleLabel(): TextView {

        return titleLabel
    }

    fun populationTitleLabel(): TextView {

        return populationTitleLabel
    }

    fun populationLabel(): TextView {

        return populationLabel
    }

    fun mapView(): MapView {

        return mapView
    }

    fun imageCarouselView(): ImageCarouselView {

        return imageCarouselView
    }

    fun shimmeringLoader(): ShimmeringLoaderView {

        return shimmeringLoader
    }

    fun viewModel(): CityDetailsViewModel {

        return viewModel
    }

    fun binder(): ViewBinder {
        
        return viewBinder
    }

    fun detailsViewIsCreated(contentView: ConstraintLayout = this.contentView,
                             titleLabel: TextView = this.titleLabel,
                             populationTitleLabel: TextView = this.populationTitleLabel,
                             populationLabel: TextView = this.populationLabel,
                             mapView: MapView = this.mapView,
                             imageCarouselView: ImageCarouselView = this.imageCarouselView,
                             shimmeringLoader: ShimmeringLoaderView = this.shimmeringLoader,
                             viewModel: CityDetailsViewModel = this.viewModel,
                             binder: ViewBinder = this.viewBinder): CityDetailsView {

        return CityDetailsView(view, contentView, titleLabel, populationTitleLabel, populationLabel, mapView, imageCarouselView, shimmeringLoader, viewModel, binder, constraintSetFactory, measureConverter)
    }

    fun titleLabelConstraintsToTopLeftSafeAreaOf(titleLabel: TextView, contentView: ConstraintLayout): () -> Unit {

        return {

            verify(constraints, atLeastOnce()).connect(titleLabel.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            verify(constraints, atLeastOnce()).connect(titleLabel.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            verify(constraints, atLeastOnce()).constrainWidth(titleLabel.id, ConstraintSet.WRAP_CONTENT)
            verify(constraints, atLeastOnce()).constrainHeight(titleLabel.id, ConstraintSet.WRAP_CONTENT)
        }
    }

    fun populationTitleLabelConstraintsToLeftAlignAndSpaceBelow(populationTitleLabel: TextView, titleLabel: TextView): () -> Unit {

        return {

            verify(constraints, atLeastOnce()).connect(populationTitleLabel.id, ConstraintSet.LEFT, titleLabel.id, ConstraintSet.LEFT)
            verify(constraints, atLeastOnce()).connect(populationTitleLabel.id, ConstraintSet.TOP, titleLabel.id, ConstraintSet.BOTTOM, measureConverter.convertToPixels(32))
            verify(constraints, atLeastOnce()).constrainWidth(populationTitleLabel.id, ConstraintSet.WRAP_CONTENT)
            verify(constraints, atLeastOnce()).constrainHeight(populationTitleLabel.id, ConstraintSet.WRAP_CONTENT)
        }
    }

    fun populationLabelConstraintsToRightAndVerticallyCenteredWith(populationLabel: TextView, populationTitleLabel: TextView): () -> Unit {

        return {

            verify(constraints, atLeastOnce()).connect(populationLabel.id, ConstraintSet.LEFT, populationTitleLabel.id, ConstraintSet.RIGHT, measureConverter.convertToPixels(8))
            verify(constraints, atLeastOnce()).connect(populationLabel.id, ConstraintSet.TOP, populationTitleLabel.id, ConstraintSet.TOP)
            verify(constraints, atLeastOnce()).connect(populationLabel.id, ConstraintSet.BOTTOM, populationTitleLabel.id, ConstraintSet.BOTTOM)
            verify(constraints, atLeastOnce()).constrainWidth(populationLabel.id, ConstraintSet.WRAP_CONTENT)
            verify(constraints, atLeastOnce()).constrainHeight(populationLabel.id, ConstraintSet.WRAP_CONTENT)
        }
    }

    fun mapViewConstraintsToTopRightWithHalfWidthAnd2to1AspectRatio(mapView: MapView, contentView: ConstraintLayout): () -> Unit {

        return {

            verify(constraints, atLeastOnce()).connect(mapView.view.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            verify(constraints, atLeastOnce()).connect(mapView.view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            verify(constraints, atLeastOnce()).constrainPercentWidth(mapView.view.id, 0.5f)
            verify(constraints, atLeastOnce()).setDimensionRatio(mapView.view.id, "H,2:1")
        }
    }

    fun imageCarouselViewConstraintsSafeAreaBottomAndEdgesOfAndSpacedBelowWithCorrectHeight(imageCarouselView: ImageCarouselView, contentView: ConstraintLayout, mapView: MapView): () -> Unit {

        return {

            verify(constraints, atLeastOnce()).connect(imageCarouselView.view.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            verify(constraints, atLeastOnce()).connect(imageCarouselView.view.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            verify(constraints, atLeastOnce()).connect(imageCarouselView.view.id, ConstraintSet.TOP, mapView.view.id, ConstraintSet.BOTTOM)
            verify(constraints, atLeastOnce()).connect(imageCarouselView.view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            verify(constraints, atLeastOnce()).constrainHeight(imageCarouselView.view.id, measureConverter.convertToPixels(256))
        }
    }

    fun shimmeringLoaderConstrainedToMatchFrameOfImageCarousel(shimmeringLoader: ShimmeringLoaderView, imageCarouselView: ImageCarouselView): () -> Unit {

        return {

            verify(constraints, atLeastOnce()).connect(shimmeringLoader.view.id, ConstraintSet.LEFT, imageCarouselView.view.id, ConstraintSet.LEFT)
            verify(constraints, atLeastOnce()).connect(shimmeringLoader.view.id, ConstraintSet.RIGHT, imageCarouselView.view.id, ConstraintSet.RIGHT)
            verify(constraints, atLeastOnce()).connect(shimmeringLoader.view.id, ConstraintSet.TOP, imageCarouselView.view.id, ConstraintSet.TOP)
            verify(constraints, atLeastOnce()).connect(shimmeringLoader.view.id, ConstraintSet.BOTTOM, imageCarouselView.view.id, ConstraintSet.BOTTOM)
        }
    }

    fun viewModelPublishesShowLoader(viewModel: CityDetailsViewModel) {

        viewModelShowLoader.onNext(true)
    }

    fun viewModelPublishesHideLoader(viewModel: CityDetailsViewModel) {

        viewModelShowLoader.onNext(false)
    }

    fun shimmeringLoaderStartedAnimating(shimmeringLoader: ShimmeringLoaderView) {

        verify(shimmeringLoader).startAnimating()
    }

    fun shimmeringLoaderStopppedAnimating(shimmeringLoader: ShimmeringLoaderView) {

        verify(shimmeringLoader).stopAnimating()
    }

    fun detailsViewHasExpectedConstraints(detailsView: CityDetailsView, expectedConstraints: () -> Unit) {

        expectedConstraints()
    }

    fun titleLabelIsBoundToViewModel(titleLabel: TextView, viewModel: CityDetailsViewModel) {

        verify(viewBinder).bindTextView(titleLabel, viewModelTitle)
    }

    fun populationTitleLabelIsBoundToViewModel(populationTitleLabel: TextView, viewModel: CityDetailsViewModel) {

        verify(viewBinder).bindTextView(populationTitleLabel, viewModelPopulationTitle)
    }

    fun populationLabelIsBoundToViewModel(populationLabel: TextView, viewModel: CityDetailsViewModel) {

        verify(viewBinder).bindTextView(populationLabel, viewModelPopulation)
    }
}