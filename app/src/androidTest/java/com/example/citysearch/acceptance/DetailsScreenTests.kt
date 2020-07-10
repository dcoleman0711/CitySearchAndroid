package com.example.citysearch.acceptance

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.R
import com.example.citysearch.ui.ShimmeringLoaderView
import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.entities.GeoPoint
import com.example.citysearch.entities.ImageSearchResults
import com.example.citysearch.services.ImageSearchService
import com.example.citysearch.models.CityDetailsModelImp
import com.example.citysearch.ui.CityDetailsView
import com.example.citysearch.ui.CityDetailsViewImp
import com.example.citysearch.viewmodels.CityDetailsViewModelImp
import com.example.citysearch.models.ImageCarouselModelImp
import com.example.citysearch.ui.ImageCarouselView
import com.example.citysearch.ui.MapView
import com.example.citysearch.reactive.ViewBinderImp
import com.example.citysearch.stub.CitySearchResultsStub
import com.example.citysearch.utilities.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

class DetailsScreenTestConstants {

    companion object {

        val titleFont = Font(Typeface.DEFAULT, 36.0)
        val populationTitleFont = Font(Typeface.DEFAULT, 24.0)
        val populationTitleText = "Population: "
    }
}

@RunWith(AndroidJUnit4::class)
class DetailsScreenTests {

    lateinit var steps: DetailsScreenSteps

    val Given: DetailsScreenSteps get() = steps
    val When: DetailsScreenSteps get() = steps
    val Then: DetailsScreenSteps get() = steps

    @Before
    fun setUp() {
        
        steps = DetailsScreenSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testBackgroundIsWhite() {

        Given.detailsScreen()

        val detailsScreen = When.detailsScreenIsLoaded()

        Then.detailsScreenBackgroundIsLightGrayWhite(detailsScreen)
    }

    @Test
    fun testTitlePosition() {

        val titleLabel = Given.titleLabel()
        Given.detailsScreen(titleLabel = titleLabel)
        val detailsScreen = Given.detailsScreenIsLoaded()

        When.detailsScreenIsLayedOut(detailsScreen)

        Then.titleLabelIsInTopLeftCornerOfScreen(titleLabel, detailsScreen)
    }

    @Test
    fun testTitleFont() {

        val titleLabel = Given.titleLabel()
        val titleFont = Given.titleFont()
        Given.detailsScreen(titleLabel = titleLabel)

        val detailsScreen = When.detailsScreenIsLoaded()

        Then.labelFontIs(titleLabel, titleFont)
    }

    @Test
    fun testTitleText() {

        val titleLabel = Given.titleLabel()
        val searchResult = Given.searchResult()
        val titleText = Given.titleText(searchResult)
        Given.detailsScreen(searchResult = searchResult, titleLabel = titleLabel)

        val detailsScreen = When.detailsScreenIsLoaded()

        Then.labelTextIs(titleLabel, titleText)
    }

    @Test
    fun testPopulationTitlePosition() {

        val populationTitleLabel = Given.populationTitleLabel()
        val titleLabel = Given.titleLabel()
        Given.detailsScreen(titleLabel = titleLabel, populationTitleLabel = populationTitleLabel)
        val detailsScreen = Given.detailsScreenIsLoaded()

        When.detailsScreenIsLayedOut(detailsScreen)

        Then.populationTitleLabelIsLeftAlignedAndSpacedBelowTitle(populationTitleLabel, titleLabel, detailsScreen)
    }

    @Test
    fun testPopulationTitleFont() {

        val populationTitleLabel = Given.populationTitleLabel()
        val populationTitleFont = Given.populationFont()
        Given.detailsScreen(populationTitleLabel = populationTitleLabel)

        val detailsScreen = When.detailsScreenIsLoaded()

        Then.labelFontIs(populationTitleLabel, populationTitleFont)
    }

    @Test
    fun testPopulationTitleText() {

        val populationTitleLabel = Given.populationTitleLabel()
        val populationTitleText = Given.populationTitleText()
        Given.detailsScreen(populationTitleLabel = populationTitleLabel)

        val detailsScreen = When.detailsScreenIsLoaded()

        Then.labelTextIs(populationTitleLabel, populationTitleText)
    }

    @Test
    fun testPopulationPosition() {

        val populationLabel = Given.populationLabel()
        val populationTitleLabel = Given.populationTitleLabel()
        Given.detailsScreen(populationTitleLabel = populationTitleLabel, populationLabel = populationLabel)
        val detailsScreen = Given.detailsScreenIsLoaded()

        When.detailsScreenIsLayedOut(detailsScreen)

        Then.populationLabelIsVerticallyAlignedAndSpacedNextToPopulationTitle(populationLabel, populationTitleLabel, detailsScreen)
    }

    @Test
    fun testPopulationFont() {

        val populationLabel = Given.populationLabel()
        val populationFont = Given.populationFont()
        Given.detailsScreen(populationLabel = populationLabel)

        val detailsScreen = When.detailsScreenIsLoaded()

        Then.labelFontIs(populationLabel, populationFont)
    }

    @Test
    fun testPopulationText() {

        val populationLabel = Given.populationLabel()
        val searchResult = Given.searchResult()
        val populationText = Given.populationText(searchResult)
        Given.detailsScreen(searchResult = searchResult, populationLabel = populationLabel)

        val detailsScreen = When.detailsScreenIsLoaded()

        Then.labelTextIs(populationLabel, populationText)
    }

    @Test
    fun testMapPosition() {

        val map = Given.map()
        Given.detailsScreen(map = map)
        val detailsScreen = Given.detailsScreenIsLoaded()

        When.detailsScreenIsLayedOut(detailsScreen)

        Then.mapIsInTopRightCornerOfScreen(map, detailsScreen)
    }

    @Test
    fun testMapSize() {

        val map = Given.map()
        Given.detailsScreen(map = map)
        val detailsScreen = Given.detailsScreenIsLoaded()

        When.detailsScreenIsLayedOut(detailsScreen)

        Then.mapIsHalfWidthWithCorrectAspectRatio(map, detailsScreen)
    }

    @Test
    fun testImageCarouselPosition() {

        val imageCarousel = Given.imageCarousel()
        val map = Given.map()
        Given.detailsScreen(map = map, imageCarousel = imageCarousel)
        val detailsScreen = Given.detailsScreenIsLoaded()

        When.detailsScreenIsLayedOut(detailsScreen)

        Then.imageCarouselIsOnLeftEdgeOfViewAndSpacedBelowMap(imageCarousel, detailsScreen, map)
    }

    @Test
    fun testImageCarouselSize() {

        val imageCarousel = Given.imageCarousel()
        Given.detailsScreen(imageCarousel = imageCarousel)
        val detailsScreen = Given.detailsScreenIsLoaded()

        When.detailsScreenIsLayedOut(detailsScreen)

        Then.imageCarouselIsWidthOfViewWithCorrectHeight(imageCarousel, detailsScreen)
    }
}

class DetailsScreenSteps(private val context: Context) {

    private val detailsView = LayoutInflater.from(context).inflate(R.layout.citydetails, null) as ConstraintLayout

    private val titleLabel = detailsView.findViewById<TextView>(R.id.titleLabel)
    private val populationTitleLabel = detailsView.findViewById<TextView>(R.id.populationTitleLabel)
    private val populationLabel = detailsView.findViewById<TextView>(R.id.populationLabel)

    private val mapViewView = detailsView.findViewById<View>(R.id.mapView)
    private val mapView = mock<MapView> {

        on { view }.thenReturn(mapViewView)
    }

    private val imageCarouselViewView = detailsView.findViewById<View>(R.id.imageCarouselView)
    private val imageCarouselView = mock<ImageCarouselView> {

        on { view }.thenReturn(imageCarouselViewView)
    }

    private val shimmeringLoaderViewView = detailsView.findViewById<View>(R.id.shimmeringLoader)
    private val shimmeringLoaderView = mock<ShimmeringLoaderView> {

        on { view }.thenReturn(shimmeringLoaderViewView)
    }

    private val imageSearchFuture = BehaviorSubject.create<ImageSearchResults>()

    private val imageSearchService = mock<ImageSearchService> {

        on { imageSearch(any()) }.thenReturn(imageSearchFuture)
    }

    private val measureConverter = MeasureConverterImp(context)

    private lateinit var buildDetailsScreen: () -> CityDetailsView
    private lateinit var detailsScreen: CityDetailsView

    fun titleLabel(): TextView {

        return titleLabel
    }

    fun populationTitleLabel(): TextView {

        return populationTitleLabel
    }

    fun populationLabel(): TextView {

        return populationLabel
    }

    fun titleFont(): Font {

        return DetailsScreenTestConstants.titleFont
    }

    fun map(): MapView {

        return mapView
    }

    fun imageCarousel(): ImageCarouselView {

        return imageCarouselView
    }

    fun populationTitleText(): String {

        return DetailsScreenTestConstants.populationTitleText
    }

    fun populationFont(): Font {

        return DetailsScreenTestConstants.populationTitleFont
    }

    fun populationText(searchResult: CitySearchResult): String {

        return "1,234,567"
    }

    fun searchResult(): CitySearchResult {

        return CitySearchResult(
            "Test City",
            1234567,
            GeoPoint(0.0, 0.0),
            adminCode = "BS"
        )
    }

    fun titleText(searchResult: CitySearchResult): String {

        return searchResult.nameAndState
    }

    fun detailsScreen(searchResult: CitySearchResult = CitySearchResultsStub.stubResults().results[0],
                      titleLabel: TextView = this.titleLabel,
                      populationTitleLabel: TextView = this.populationTitleLabel,
                      populationLabel: TextView = this.populationLabel,
                      map: MapView = this.mapView,
                      imageCarousel: ImageCarouselView = this.imageCarouselView) {

        val model = CityDetailsModelImp(
            searchResult,
            ImageCarouselModelImp(context),
            imageSearchService,
            Schedulers.computation()
        )
        val viewModel =
            CityDetailsViewModelImp(model)

        buildDetailsScreen = {

            CityDetailsViewImp(
                detailsView,
                titleLabel,
                populationTitleLabel,
                populationLabel,
                map,
                imageCarousel,
                shimmeringLoaderView,
                viewModel,
                ViewBinderImp()
            )
        }
    }

    fun detailsScreenIsLoaded(): CityDetailsView {

        detailsScreen = buildDetailsScreen()
        return detailsScreen
    }

    fun detailsScreenIsLayedOut(detailsScreen: CityDetailsView) {

        val width = 1024
        val height = 768

        detailsView.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
        detailsView.layout(0, 0, width, height)
    }

    fun detailsScreenBackgroundIsLightGrayWhite(detailsScreen: CityDetailsView) {

        val detailsView = detailsScreen.view
        val backgroundColor = (detailsView.background as ColorDrawable).color

        Assert.assertEquals("Details screen background is not white", Color.argb(1.0f, 0.8f, 0.8f, 0.8f), backgroundColor)
    }

    fun titleLabelIsInTopLeftCornerOfScreen(titleLabel: TextView, screen: CityDetailsView) {

        val isInTopLeftCorner =
            titleLabel.left == measureConverter.convertToPixels(16) &&
            titleLabel.top == measureConverter.convertToPixels(16)

        Assert.assertTrue("Title label is not in top-left corner of screen", isInTopLeftCorner)
    }

    fun labelFontIs(titleLabel: TextView, expectedFont: Font) {

        Assert.assertEquals("Title label typeface is not expected typeface", expectedFont.typeface, titleLabel.typeface)
        Assert.assertEquals("Title label font size is not expected font size", measureConverter.convertToPixels(expectedFont.size.toInt()).toFloat(), titleLabel.textSize)
    }

    fun labelTextIs(titleLabel: TextView, expectedText: String) {

        Assert.assertEquals("Label text is not correct", expectedText, titleLabel.text)
    }

    fun populationTitleLabelIsLeftAlignedAndSpacedBelowTitle(populationTitleLabel: TextView, titleLabel: TextView, detailsView: CityDetailsView) {

        val expectedOrigin = Point(titleLabel.left, titleLabel.bottom + 32)

        val originIsCorrect =
            populationTitleLabel.left == titleLabel.left &&
            populationTitleLabel.top == titleLabel.bottom + measureConverter.convertToPixels(32)

        Assert.assertTrue("Population title label is not positioned correctly", originIsCorrect)
    }

    fun populationLabelIsVerticallyAlignedAndSpacedNextToPopulationTitle(populationLabel: TextView,  populationTitleLabel: TextView, detailsScreen: CityDetailsView) {

        val originIsCorrect =
            populationLabel.left == populationTitleLabel.right + measureConverter.convertToPixels(8) &&
            populationLabel.top == (populationTitleLabel.top + populationTitleLabel.bottom) / 2 - populationLabel.height / 2

        Assert.assertTrue("Population label is not positioned correctly", originIsCorrect)
    }

    fun mapIsInTopRightCornerOfScreen(map: MapView, detailsScreen: CityDetailsView) {

        val topRightIsCorrect =
            mapViewView.right == detailsView.width - measureConverter.convertToPixels(16) &&
            mapViewView.top == measureConverter.convertToPixels(16)

        Assert.assertTrue("Map is not positioned correctly", topRightIsCorrect)
    }

    fun mapIsHalfWidthWithCorrectAspectRatio(map: MapView, detailsScreen: CityDetailsView) {

        Assert.assertEquals("Map is not half-width of details screen", detailsView.width / 2, mapViewView.width)
        Assert.assertEquals("Map does not have the correct aspect ratio", mapViewView.width / 2, mapViewView.height)
    }

    fun imageCarouselIsOnLeftEdgeOfViewAndSpacedBelowMap(imageCarousel: ImageCarouselView, detailsScreen: CityDetailsView, map: MapView) {

        Assert.assertEquals("Image carousel is not on left edge of details screen screen", 0, imageCarouselViewView.left)
        Assert.assertEquals("Image carousel is not spaced below map", mapViewView.bottom + measureConverter.convertToPixels(16), imageCarouselViewView.top)
    }

    fun imageCarouselIsWidthOfViewWithCorrectHeight(imageCarousel: ImageCarouselView, detailsScreen: CityDetailsView) {

        Assert.assertEquals("Image carousel is not width of details screen screen", detailsView.width, imageCarouselViewView.width)
        Assert.assertEquals("Image carousel does not have the correct height", detailsView.bottom - measureConverter.convertToPixels(16), imageCarouselViewView.bottom)
    }
}