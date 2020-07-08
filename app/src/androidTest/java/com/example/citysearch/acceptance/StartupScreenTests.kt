//package com.example.citysearch.acceptance
//
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.ConditionVariable
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.R
import com.example.citysearch.animations.RollingAnimationLabel
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.data.CitySearchService
import com.example.citysearch.startup.*
import com.example.citysearch.utilities.Font
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.Size
import com.example.citysearch.utilities.ViewUtilities
import com.nhaarman.mockitokotlin2.*
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.Instant

class StartupScreenTestConstants {

    companion object {

        val appTitle = "City Search"
        val appTitleFont = Font(typeface = Typeface.DEFAULT, size = 64.0)
        val maximumTransitionStartInterval = Duration.ofMillis(4500)
        val minimumTransitionStartInterval = Duration.ofMillis(3500)
    }
}

@RunWith(AndroidJUnit4::class)
class StartupScreenTests {

    lateinit var steps: StartupScreenSteps

    val Given: StartupScreenSteps get() = steps
    val When: StartupScreenSteps get() = steps
    val Then: StartupScreenSteps get() = steps

    @Before
    fun setUp() {

        steps = StartupScreenSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testBackgroundIsWhite() {

        Given.startupScreen()

        val startupScreen = When.startupScreenIsShown()

        Then.startupScreenBackgroundIsWhite(startupScreen)
    }

    @Test
    fun testAppTitleText() {

        val appTitleLabel = Given.appTitleLabel()
        Given.startupScreen(appTitleLabel = appTitleLabel)
        val appTitleText = Given.appTitleText()

        When.startupScreenIsShown()

        Then.appTitleLabelTextIs(appTitleLabel, appTitleText)
    }

    @Test
    fun testAppTitleFont() {

        val appTitleLabel = Given.appTitleLabel()
        Given.startupScreen(appTitleLabel = appTitleLabel)
        val appTitleFont = Given.appTitleFont()

        When.startupScreenIsShown()

        Then.appTitleLabelFontIs(appTitleLabel, appTitleFont)
    }

    @Test
    fun testTransitionStartsBeforeMaximumInterval() {

        val startupTransitionCommand = Given.startupTransitionCommand()
        val searchService = Given.searchService()
        val initialResults = Given.initialResults(searchService)
        Given.startupScreen(transitionCommand = startupTransitionCommand, searchService = searchService)
        val maximumTransitionStartInterval = Given.maximumTransitionStartInterval()
        val startupScreenLoadTime = Given.startupScreenLoadedAtTime()
        Given.searchServiceHasReturnedInitialResults(searchService)

        When.currentTimeIs(startupScreenLoadTime + maximumTransitionStartInterval)

        Then.transitionToCitySearchScreenHasStarted(initialResults)
    }

    @Test
    fun testTransitionDoesNotStartBeforeMinimumInterval() {

        val startupTransitionCommand = Given.startupTransitionCommand()
        val searchService = Given.searchService()
        Given.startupScreen(transitionCommand = startupTransitionCommand, searchService = searchService)
        val minimumTransitionStartInterval = Given.minimumTransitionStartInterval()
        val startupScreenLoadTime = Given.startupScreenLoadedAtTime()
        Given.searchServiceHasReturnedInitialResults(searchService)

        When.currentTimeIs(startupScreenLoadTime + minimumTransitionStartInterval)

        Then.transitionToCitySearchScreenHasNotStarted()
    }

    fun testTransitionDoesNotStartBeforeResultsAreReady() {

        val startupTransitionCommand = Given.startupTransitionCommand()
        val searchService = Given.searchService()
        val startupScreen = Given.startupScreen(transitionCommand = startupTransitionCommand, searchService =searchService)
        val maximumTransitionStartInterval = Given.maximumTransitionStartInterval()
        val startupScreenLoadTime = Given.startupScreenLoadedAtTime()

        When.currentTimeIs(startupScreenLoadTime + maximumTransitionStartInterval)

        Then.transitionToCitySearchScreenHasNotStarted()
    }

    fun testTransitionStartsWhenResultsAreReady() {

        val startupTransitionCommand = Given.startupTransitionCommand()
        val searchService = Given.searchService()
        val initialResults = Given.initialResults(searchService)
        Given.startupScreen(transitionCommand = startupTransitionCommand, searchService = searchService)
        val maximumTransitionStartInterval = Given.maximumTransitionStartInterval()
        val resultsReceivedInterval = Given.intervalGreaterThan(maximumTransitionStartInterval)
        val startupScreenLoadTime = Given.startupScreenLoadedAtTime()
        Given.searchServiceReturnsResultsAtTime(searchService, startupScreenLoadTime + resultsReceivedInterval)

        When.searchServiceReturnsInitialResults(searchService)

        Then.transitionToCitySearchScreenHasStarted(initialResults)
    }
}

class StartupScreenSteps(private val context: Context) {

    private val initialResults = CitySearchResults.emptyResults()

    private val startupViewModelFactory = StartupViewModelFactory(mock())

    private lateinit var startupViewModel: StartupViewModelImp

    private val transitionCommand = mock<StartupTransitionCommand> {  }

    private val appTitleLabel = RollingAnimationLabelTest(context)

    private val searchResultsFuture = BehaviorSubject.create<CitySearchResults>()

    private val resultsArrivedCondition = ConditionVariable()

    private val searchService = mock<CitySearchService> {

        on { citySearch() }.thenReturn(searchResultsFuture)
    }
    
    lateinit var startupScreen: StartupView

    fun currentTimeIs(time: Instant) {

        val timeToWait = Duration.between(Instant.now(), time);

        Thread.sleep(timeToWait.toMillis())
    }

    fun searchServiceReturnsInitialResults(searchService: CitySearchService) {

        resultsArrivedCondition.block(30000)
    }

    fun startupTransitionCommand(): StartupTransitionCommand {

        return transitionCommand
    }

    fun searchService(): CitySearchService {

        return searchService
    }

    fun initialResults(searchService: CitySearchService): CitySearchResults {

        return initialResults
    }

    fun maximumTransitionStartInterval(): Duration {

        return StartupScreenTestConstants.maximumTransitionStartInterval
    }

    fun minimumTransitionStartInterval(): Duration {

        return StartupScreenTestConstants.minimumTransitionStartInterval
    }

    fun intervalGreaterThan(interval: Duration): Duration {

        return interval + Duration.ofSeconds(1)
    }

    fun startupScreenLoadedAtTime(): Instant {

        val view = LayoutInflater.from(context).inflate(R.layout.startup, null)
        this.startupScreen = StartupViewImp(view, startupViewModel, appTitleLabel)
        return Instant.now()
    }

    fun appTitleFont(): Font {

        return StartupScreenTestConstants.appTitleFont
    }

    fun appTitleText(): String {

        return StartupScreenTestConstants.appTitle
    }

    fun screenSizes(): Array<Size> {

        return arrayOf(Size(width = 1024, height = 768), Size(width = 2048, height = 768), Size(width = 2048, height = 1536))
    }

    fun appTitleLabel(): RollingAnimationLabel {

        return appTitleLabel
    }

    fun startupScreen(appTitleLabel: RollingAnimationLabel = this.appTitleLabel, transitionCommand: StartupTransitionCommand = this.transitionCommand, searchService: CitySearchService = this.searchService) {

        startupViewModelFactory.transitionCommand = transitionCommand
        startupViewModelFactory.searchService = searchService

        startupViewModel = startupViewModelFactory.create(StartupViewModelImp::class.java)
    }

    fun searchServiceHasReturnedInitialResults(searchService: CitySearchService) {

        searchResultsFuture.onNext(initialResults)
    }

    fun searchServiceReturnsResultsAtTime(searchService: CitySearchService, time: Instant) {

        val timeToWait = Duration.between(Instant.now(), time);

        Thread.sleep(timeToWait.toMillis())

        searchServiceHasReturnedInitialResults(searchService)
    }

    fun startupScreenIsShown(): StartupView {

        startupScreenLoadedAtTime()
        return startupScreen
    }

    fun startupScreenSizeBecomes(startupScreen: StartupView, screenSize: Size) {

        startupScreen.view.requestLayout();
        startupScreen.view.measure(View.MeasureSpec.makeMeasureSpec(screenSize.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(screenSize.height, View.MeasureSpec.EXACTLY))
        startupScreen.view.layout(0, 0, screenSize.width, screenSize.height);
    }

    fun startupScreenBackgroundIsWhite(startupScreen: StartupView) {

        val background = startupScreen.view.background
        if(background !is ColorDrawable) {
            fail("Background is not solid color")
            return
        }

        assertEquals(background.color, Color.WHITE)
    }

    fun appTitleLabelTextIs(appTitleLabel: RollingAnimationLabel, expectedText: String) {

        assertEquals("App title text is not correct", expectedText, this.appTitleLabel.text)
    }

    fun appTitleLabelFontIs(appTitleLabel: RollingAnimationLabel, expectedFont: Font) {

        assertEquals("App title font is not correct", expectedFont, this.appTitleLabel.font)
    }

    fun transitionToCitySearchScreenHasStarted(expectedResults: CitySearchResults) {

        verify(transitionCommand, times(1)).invoke(initialResults)
    }

    fun transitionToCitySearchScreenHasNotStarted() {

        verify(transitionCommand, never()).invoke(any())
    }
}

class RollingAnimationLabelTest(context: Context): RollingAnimationLabel(context, null) {

    var text: String? = null
    var font: Font? = null

    override fun start(text: String, font: Font) {

        this.text = text
        this.font = font
    }
}