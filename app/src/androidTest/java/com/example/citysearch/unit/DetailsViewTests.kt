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
    fun testTitleBindToViewModel() {

        val titleLabel = Given.titleLabel()
        val viewModel = Given.viewModel()
        val binder = Given.binder()

        val detailsView = When.detailsViewIsCreated(titleLabel = titleLabel, viewModel = viewModel, binder = binder)

        Then.titleLabelIsBoundToViewModel(titleLabel, viewModel)
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
    fun testPopulationLabelBindToViewModel() {

        val populationLabel = Given.populationLabel()
        val viewModel = Given.viewModel()
        val binder = Given.binder()

        val detailsView = When.detailsViewIsCreated(populationLabel = populationLabel, viewModel = viewModel, binder = binder)

        Then.populationLabelIsBoundToViewModel(populationLabel, viewModel)
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

    private val view = mock<ConstraintLayout> {  }

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

    fun titleLabel(): TextView {

        return titleLabel
    }

    fun populationTitleLabel(): TextView {

        return populationTitleLabel
    }

    fun populationLabel(): TextView {

        return populationLabel
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

    fun detailsViewIsCreated(titleLabel: TextView = this.titleLabel,
                             populationTitleLabel: TextView = this.populationTitleLabel,
                             populationLabel: TextView = this.populationLabel,
                             mapView: MapView = this.mapView,
                             imageCarouselView: ImageCarouselView = this.imageCarouselView,
                             shimmeringLoader: ShimmeringLoaderView = this.shimmeringLoader,
                             viewModel: CityDetailsViewModel = this.viewModel,
                             binder: ViewBinder = this.viewBinder): CityDetailsView {

        return CityDetailsView(view, titleLabel, populationTitleLabel, populationLabel, mapView, imageCarouselView, shimmeringLoader, viewModel, binder)
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