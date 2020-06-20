package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.acceptance.DetailsScreenTestConstants
import com.example.citysearch.details.CityDetailsModel
import com.example.citysearch.details.CityDetailsViewModelImp
import com.example.citysearch.reactive.TextViewModel
import com.example.citysearch.utilities.Font
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsViewModelTests {

    lateinit var steps: DetailsViewModelSteps

    val Given: DetailsViewModelSteps get() = steps
    val When: DetailsViewModelSteps get() = steps
    val Then: DetailsViewModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = DetailsViewModelSteps()
    }

    @Test
    fun testTitleFont() {

        val titleFont = Given.titleFont()
        val model = Given.model()
        val detailsViewModel = Given.detailsViewModel(model = model)

        val labelObserver = When.observeTitle(detailsViewModel)

        Then.observedFontIsEqualToFont(titleFont)
    }

    @Test
    fun testTitleText() {

        val model = Given.model()
        val titleText = Given.titleText(model)
        val detailsViewModel = Given.detailsViewModel(model = model)

        val labelObserver = When.observeTitle(detailsViewModel)

        Then.observedTextIsEqualTo(titleText)
    }

    @Test
    fun testPopulationTitleFont() {

        val populationFont = Given.populationFont()
        val model = Given.model()
        val detailsViewModel = Given.detailsViewModel(model = model)

        val labelObserver = When.observePopulationTitle(detailsViewModel)

        Then.observedFontIsEqualToFont(populationFont)
    }

    @Test
    fun testPopulationTitleText() {

        val populationTitle = Given.populationTitleText()
        val model = Given.model()
        val detailsViewModel = Given.detailsViewModel(model = model)

        val labelObserver = When.observePopulationTitle(detailsViewModel)

        Then.observedTextIsEqualTo(populationTitle)
    }

    @Test
    fun testPopulationFont() {

        val populationFont = Given.populationFont()
        val model = Given.model()
        val detailsViewModel = Given.detailsViewModel(model = model)

        val labelObserver = When.observePopulation(detailsViewModel)

        Then.observedFontIsEqualToFont(populationFont)
    }

    @Test
    fun testPopulationText() {

        val model = Given.model()
        val populationText = Given.populationText(model)
        val detailsViewModel = Given.detailsViewModel(model = model)

        val labelObserver = When.observePopulation(detailsViewModel)

        Then.observedTextIsEqualTo(populationText)
    }

    @Test
    fun testShowLoader() {

        for(show in arrayOf(false, true)) {

            testShowLoader(show)
        }
    }

    fun testShowLoader(show: Boolean) {

        val model = Given.model()
        val detailsViewModel = Given.detailsViewModel(model = model)
        val showLoaderObserver = Given.observeShowLoader(detailsViewModel)

        When.modelUpdatesLoading(model, show)

        Then.observedShowLoaderIsEqualTo(show)
    }
}

class DetailsViewModelSteps {

    private val titleText = "Test Title"
    private val population = 1234567

    private var observedLabelData: TextViewModel? = null

    private var observedLoading: Boolean? = null

    private val modelTitle = BehaviorSubject.just(titleText)
    private val modelPopulation = BehaviorSubject.just(population)
    private val modelLoading = BehaviorSubject.create<Boolean>()
    private val model = mock<CityDetailsModel> {

        on { title }.thenReturn(modelTitle)
        on { population }.thenReturn(modelPopulation)
        on { loading }.thenReturn(modelLoading)
    }

    fun titleFont(): Font {

        return DetailsScreenTestConstants.titleFont
    }

    fun populationFont(): Font {

        return DetailsScreenTestConstants.populationTitleFont
    }

    fun populationTitleText(): String {

        return DetailsScreenTestConstants.populationTitleText
    }

    fun model(): CityDetailsModel {

        return model
    }

    fun titleText(model: CityDetailsModel): String {

        return titleText
    }

    fun populationText(model: CityDetailsModel): String {

        return "1,234,567"
    }

    fun detailsViewModel(model: CityDetailsModel): CityDetailsViewModelImp {

        return CityDetailsViewModelImp(model)
    }

    fun observeTitle(viewModel: CityDetailsViewModelImp): Disposable {

        return viewModel.title.subscribe { title ->

            observedLabelData = title
        }
    }

    fun observePopulationTitle(viewModel: CityDetailsViewModelImp): Disposable {

        return viewModel.populationTitle.subscribe { title ->

            observedLabelData = title
        }
    }

    fun observePopulation(viewModel: CityDetailsViewModelImp): Disposable {

        return viewModel.population.subscribe { title ->

            observedLabelData = title
        }
    }

    fun observeShowLoader(viewModel: CityDetailsViewModelImp): Disposable {

        return viewModel.showLoader.subscribe { showLoader ->

            observedLoading = showLoader
        }
    }

    fun modelUpdatesLoading(model: CityDetailsModel, loading: Boolean) {

        modelLoading.onNext(loading)
    }

    fun observedFontIsEqualToFont(expectedFont: Font) {

        Assert.assertEquals("Observed font is not correct font", expectedFont, observedLabelData?.font)
    }

    fun observedTextIsEqualTo(expectedText: String) {

        Assert.assertEquals("Observed text is not correct text", expectedText, observedLabelData?.text)
    }

    fun observedShowLoaderIsEqualTo(expectedLoading: Boolean) {

        Assert.assertEquals("Observed loading flag is not correct value", expectedLoading, observedLoading)
    }
}