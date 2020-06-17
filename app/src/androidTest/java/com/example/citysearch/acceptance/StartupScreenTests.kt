//package com.example.citysearch.acceptance
//
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.animations.RollingAnimationLabel
import com.example.citysearch.data.CitySearchService
import com.example.citysearch.startup.StartupTransitionCommand
import com.example.citysearch.startup.StartupView
import com.example.citysearch.utilities.Font
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.Rect
import com.example.citysearch.utilities.Size
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.Duration

class StartupScreenTestConstants {

    companion object {

        val appTitle = "City Search"
        val appTitleFont = Font(typeface = Typeface.DEFAULT, size = 64.0)
        val maximumTransitionStartInterval = Duration.ofMillis(4500)
        val minimumTransitionStartInterval = Duration.ofMillis(3500)
    }
}
//
//@RunWith(AndroidJUnit4::class)
//class StartupScreenTests {
//
//    lateinit var steps: StartupScreenSteps
//
//    val Given: StartupScreenSteps get() = steps
//    val When: StartupScreenSteps get() = steps
//    val Then: StartupScreenSteps get() = steps
//
//    @Before
//    fun setUp() {
//
//        steps = StartupScreenSteps()
//    }
//
//    @Test
//    fun testBackgroundIsWhite() {
//
//        val startupScreen = Given.startupScreen()
//
//        When.startupScreenIsShown(startupScreen)
//
//        Then.startupScreenBackgroundIsWhite(startupScreen)
//    }
//
//    @Test
//    fun testAppTitleIsVisible() {
//
//        val appTitleLabel = Given.appTitleLabel()
//        val startupScreen = Given.startupScreen(appTitleLabel)
//
//        When.startupScreenIsShown(startupScreen)
//
//        Then.appTitleIsVisible(startupScreen, appTitleLabel)
//    }
//
//    @Test
//    fun testAppTitleCenter() {
//
//        val screenSizes = Given.screenSizes()
//
//        for(screenSize in screenSizes) {
//
//            testAppTitleCenter(screenSize)
//        }
//    }
//
//    fun testAppTitleCenter(screenSize: Size) {
//
//        val appTitleLabel = Given.appTitleLabel()
//        val startupScreen = Given.startupScreen(appTitleLabel)
//        Given.startupScreenIsShown(startupScreen)
//
//        When.startupScreenSizeBecomes(startupScreen, screenSize)
//        Then.appTitleLabelIsCentered(appTitleLabel, screenSize)
//    }
//
//    @Test
//    fun testAppTitleSize() {
//
//        val screenSizes = Given.screenSizes()
//
//        for(screenSize in screenSizes) {
//
//            testAppTitleSize(screenSize)
//        }
//    }
//
//    fun testAppTitleSize(screenSize: Size) {
//
//        val appTitleLabel = Given.appTitleLabel()
//        val startupScreen = Given.startupScreen(appTitleLabel)
//        Given.startupScreenIsShown(startupScreen)
//
//        When.startupScreenSizeBecomes(startupScreen, screenSize)
//        Then.appTitleLabelSizeFitsText(appTitleLabel)
//    }
//
//    @Test
//    fun testAppTitleText() {
//
//        val appTitleLabel = Given.appTitleLabel()
//        val startupScreen = Given.startupScreen(appTitleLabel)
//        val appTitleText = Given.appTitleText()
//
//        When.startupScreenIsShown(startupScreen)
//
//        Then.appTitleLabelTextIs(appTitleLabel, appTitleText)
//    }
//
//    @Test
//    fun testAppTitleFont() {
//
//        val appTitleLabel = Given.appTitleLabel()
//        val startupScreen = Given.startupScreen(appTitleLabel)
//        val appTitleFont = Given.appTitleFont()
//
//        When.startupScreenIsShown(startupScreen)
//
//        Then.appTitleLabelFontIs(appTitleLabel, appTitleFont)
//    }
//
////    fun testTransitionStartsBeforeMaximumInterval() {
////
////        val startupTransitionCommand = Given.startupTransitionCommand()
////        val searchService = Given.searchService()
////        val initialResults = Given.initialResults(from: searchService)
////        val startupScreen = Given.startupScreen(transitionCommand: startupTransitionCommand, searchService: searchService)
////        val maximumTransitionStartInterval = Given.maximumTransitionStartInterval()
////        val startupScreenLoadTime = Given.startupScreenLoadedAtTime(startupScreen)
////        Given.searchServiceHasReturnedInitialResults(searchService)
////
////        When.currentTimeIs(startupScreenLoadTime + maximumTransitionStartInterval)
////
////        Then.transitionToCitySearchScreenHasStarted(withInitialResults: initialResults)
////    }
////
////    fun testTransitionDoesNotStartBeforeMinimumInterval() {
////
////        val startupTransitionCommand = Given.startupTransitionCommand()
////        val searchService = Given.searchService()
////        val startupScreen = Given.startupScreen(transitionCommand: startupTransitionCommand, searchService: searchService)
////        val minimumTransitionStartInterval = Given.minimumTransitionStartInterval()
////        val startupScreenLoadTime = Given.startupScreenLoadedAtTime(startupScreen)
////        Given.searchServiceHasReturnedInitialResults(searchService)
////
////        When.currentTimeIs(startupScreenLoadTime + minimumTransitionStartInterval)
////
////        Then.transitionToCitySearchScreenHasNotStarted()
////    }
////
////    fun testTransitionDoesNotStartBeforeResultsAreReady() {
////
////        val startupTransitionCommand = Given.startupTransitionCommand()
////        val searchService = Given.searchService()
////        val startupScreen = Given.startupScreen(transitionCommand: startupTransitionCommand, searchService: searchService)
////        val maximumTransitionStartInterval = Given.maximumTransitionStartInterval()
////        val startupScreenLoadTime = Given.startupScreenLoadedAtTime(startupScreen)
////
////        When.currentTimeIs(startupScreenLoadTime + maximumTransitionStartInterval)
////
////        Then.transitionToCitySearchScreenHasNotStarted()
////    }
////
////    fun testTransitionStartsWhenResultsAreReady() {
////
////        val startupTransitionCommand = Given.startupTransitionCommand()
////        val searchService = Given.searchService()
////        val initialResults = Given.initialResults(from: searchService)
////        val startupScreen = Given.startupScreen(transitionCommand: startupTransitionCommand, searchService: searchService)
////        val maximumTransitionStartInterval = Given.maximumTransitionStartInterval()
////        val resultsReceivedInterval = Given.intervalGreaterThan(maximumTransitionStartInterval)
////        val startupScreenLoadTime = Given.startupScreenLoadedAtTime(startupScreen)
////        Given.searchService(searchService, returnsResultsAt: startupScreenLoadTime + resultsReceivedInterval)
////
////        When.searchServiceReturnsInitialResults(searchService)
////
////        Then.transitionToCitySearchScreenHasStarted(withInitialResults: initialResults)
////    }
//}
//
//class StartupScreenSteps {
//
////    private val initialResults = CitySearchResults.emptyResults()
////
////    private var transitionInvocationResults: CitySearchResults? = null
////
////    private lateinit var servicePromise: CitySearchService.SearchFuture.Promise
//
//    @get:Rule var activityScenario = launchActivity<StartupView>()
//
//    lateinit var startupScreen: StartupView
//
//    @Captor
//    private lateinit var titleLabelText: ArgumentCaptor<String>
//
//    @Captor
//    private lateinit var titleLabelFont: ArgumentCaptor<Font>
//
//    init {
//
//        MockitoAnnotations.initMocks(this);
//
//        activityScenario.onActivity { activity ->
//            startupScreen = activity
//        }
//    }
////    fun currentTimeIs(time: Instant) {
////
////        val timeToWait = Duration.between(Instant.now(), time);
////
////        Thread.sleep(timeToWait.toMillis())
////    }
////
////    fun searchServiceReturnsInitialResults(_ searchService: CitySearchService) {
////
////        val expectation = tests.expectation(description: "Search results returned")
////
////        this.resultsReturnedExpectation = expectation
////
////        tests.wait(for: [expectation], timeout: 30.0)
////    }
////
////    fun startupTransitionCommand() -> StartupTransitionCommand {
////
////        val command = StartupTransitionCommand()
////
////        command.invokeImp = { initialResults in
////
////                this.transitionInvocationResults = initialResults
////        }
////
////        return command
////    }
////
////    fun searchService() -> CitySearchService {
////
////        val searchService = CitySearchService()
////
////        val serviceFuture = CitySearchService.SearchFuture({ promise in
////
////                this.servicePromise = { results in
////
////                promise(results)
////
////            // val the run loop run before fulfilling, because the results are dispatched on the main run loop
////            DispatchQueue.main.async {
////
////                this.resultsReturnedExpectation?.fulfill()
////            }
////        }
////        })
////
////        searchService.citySearchImp = { serviceFuture }
////
////        return searchService
////    }
////
////    fun initialResults(from searchService: CitySearchService) -> CitySearchResults {
////
////        initialResults
////    }
////
////    fun maximumTransitionStartInterval() -> TimeInterval {
////
////        StartupScreenTestConstants.maximumTransitionStartInterval
////    }
////
////    fun minimumTransitionStartInterval() -> TimeInterval {
////
////        StartupScreenTestConstants.minimumTransitionStartInterval
////    }
////
////    fun intervalGreaterThan(_ interval: TimeInterval) -> TimeInterval {
////
////        interval + 1.0
////    }
////
////    fun startupScreenLoadedAtTime(_ startupScreen: StartupView) -> Date {
////
////        startupScreen.loadViewIfNeeded()
////        return Date()
////    }
//
//    fun appTitleFont(): Font {
//
//        return StartupScreenTestConstants.appTitleFont
//    }
//
//    fun appTitleText(): String {
//
//        return StartupScreenTestConstants.appTitle
//    }
//
//    fun screenSizes(): Array<Size> {
//
//        return arrayOf(Size(width = 1024, height = 768), Size(width = 2048, height = 768), Size(width = 2048, height = 1536))
//    }
//
//    fun appTitleLabel(): RollingAnimationLabel {
//
//        return mock(RollingAnimationLabel::class.java)
//    }
//
////    fun landscapeOrientations() -> UIInterfaceOrientationMask {
////
////        UIInterfaceOrientationMask.landscape
////    }
//
//    fun startupScreen(appTitleLabel: RollingAnimationLabel = mock(RollingAnimationLabel::class.java), transitionCommand: StartupTransitionCommand = mock(StartupTransitionCommand::class.java), searchService: CitySearchService = mock(CitySearchService::class.java)): StartupView {
//
//        startupScreen.appTitleLabel = appTitleLabel
////        builder.transitionCommand = transitionCommand
////        builder.searchService = searchService
//
//        return startupScreen
//    }
//
////    fun searchServiceHasReturnedInitialResults(_ searchService: CitySearchService) {
////
////        servicePromise(.success(initialResults))
////    }
////
////    fun searchService(_ searchService: CitySearchService, returnsResultsAt time: Date) {
////
////        val timeToWait = time.timeIntervalSinceNow
////
////                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + timeToWait, execute: {
////
////            this.servicePromise(.success(this.initialResults))
////        })
////    }
//
//    fun startupScreenIsShown(startupScreen: StartupView) {
//
//        activityScenario.moveToState(Lifecycle.State.STARTED)
//    }
//
//    fun startupScreenSizeBecomes(startupScreen: StartupView, screenSize: Size) {
//
//        startupScreen.view.requestLayout();
//        startupScreen.view.measure(View.MeasureSpec.makeMeasureSpec(screenSize.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(screenSize.height, View.MeasureSpec.EXACTLY))
//        startupScreen.view.layout(0, 0, screenSize.width, screenSize.height);
//    }
//
//    fun startupScreenBackgroundIsWhite(startupScreen: StartupView) {
//
//        val background = startupScreen.view.background
//        if(background !is ColorDrawable) {
//            fail("Background is not solid color")
//            return
//        }
//
//        assertEquals(background.color, Color.WHITE)
//    }
//
//    fun appTitleIsVisible(startupScreen: StartupView, appTitleLabel: RollingAnimationLabel) {
//
//        assertTrue("App title is not visible on startup screen",  ViewUtilities.isDescendantOf(appTitleLabel, startupScreen.view))
//    }
//
//    fun appTitleLabelIsCentered(appTitleLabel: RollingAnimationLabel, screenSize: Size) {
//
//        val center = ViewUtilities.center(appTitleLabel)
//        assertEquals("App title center is not screen center", ViewUtilities.center(appTitleLabel), Point(x = screenSize.width / 2, y = screenSize.height / 2))
//    }
//
//    fun appTitleLabelTextIs(appTitleLabel: RollingAnimationLabel, expectedText: String) {
//
//        verify(appTitleLabel).start(titleLabelText.capture(), Font(Typeface.DEFAULT, 12.0))
//
//        assertEquals("App title text is not app title", titleLabelText, expectedText)
//    }
//
//    fun appTitleLabelSizeFitsText(appTitleLabel: RollingAnimationLabel) {
//
//        assertEquals("App title size does not fit text", ViewUtilities.frame(appTitleLabel).size, ViewUtilities.intrinsicSize(appTitleLabel))
//    }
//
//    fun appTitleLabelFontIs(appTitleLabel: RollingAnimationLabel, font: Font) {
//
//        verify(appTitleLabel).start("", titleLabelFont.capture())
//
//        assertEquals("App title font is not correct", titleLabelFont, font)
//    }
//
////    fun transitionToCitySearchScreenHasStarted(expectedResults: CitySearchResults) {
////
////        guard val invocationResults = transitionInvocationResults else {
////            XCTFail("Transition to search screen has not started with initial results")
////            return
////        }
////
////        assertEquals(invocationResults, expectedResults, "Transition was started with incorrect results")
////    }
////
////    fun transitionToCitySearchScreenHasNotStarted() {
////
////        XCTAssertNil(transitionInvocationResults, "Transition to search screen has already started")
////    }
//}
//