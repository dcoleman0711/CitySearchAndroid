package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.acceptance.StartupScreenTestConstants
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.data.CitySearchService
import com.example.citysearch.reactive.ObservableFactory
import com.example.citysearch.startup.StartupModelImp
import com.example.citysearch.startup.StartupTransitionCommand
import com.example.citysearch.stub.CitySearchResultsStub
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import java.time.Duration
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class StartupModelTests {

    lateinit var steps: StartupModelSteps

    val Given: StartupModelSteps get() = steps
    val When: StartupModelSteps get() = steps
    val Then: StartupModelSteps get() = steps

    @Before
    fun setUp() {

        steps = StartupModelSteps()
    }

    @Test
    fun testObserveAppTitleText() {

        val appTitle = Given.appTitle()
        val model = Given.modelIsCreated()

        When.observeAppTitleText(model)

        Then.appTitleTextIsUpdated(appTitle)
    }

    @Test
    fun testScheduleTransitionTimer() {

        val model = Given.modelIsCreated()
        val transitionStartInterval = Given.transitionStartInterval()

        When.transitionIsScheduled(model)

        Then.timerIsScheduled(transitionStartInterval)
    }

    @Test
    fun testTransitionTimerCallsTransitionCommandIfInitialResultsAreReady() {

        val searchService = Given.searchService()
        val initialResults = Given.initialResults(searchService)
        val transitionCommand = Given.transitionCommand()
        val invocationQueue = Given.invokeScheduler()
        val model = Given.modelIsCreated(transitionCommand, searchService)
        Given.transitionIsScheduled(model)
        Given.searchServiceHasReturnedResults(searchService)

        When.transitionTimerFires()

        Then.transitionCommand(transitionCommand, initialResults, invocationQueue)
    }

    @Test
    fun testTransitionTimerDoesNotCallTransitionCommandIfInitialResultsAreNotReady() {

        val searchService = Given.searchService()
        val transitionCommand = Given.transitionCommand()
        val model = Given.modelIsCreated(transitionCommand, searchService)
        Given.transitionIsScheduled(model)

        When.transitionTimerFires()

        Then.transitionCommandIsNotInvoked(transitionCommand)
    }

    @Test
    fun testRequestInitialResults() {

        val searchService = Given.searchService()

        val model = When.modelIsCreated(searchService = searchService)

        Then.searchServiceResultsWereRequested(searchService)
    }
}

class StartupModelSteps {

    private val initialResults = CitySearchResultsStub.stubResults()

    private val transitionCommand = mock<StartupTransitionCommand>()

    private val timer = BehaviorSubject.create<Long>()

    private val observableFactory = mock<ObservableFactory> {

        on { timer(any(), any(), any()) }.thenReturn(timer)
    }

    private val workScheduler = TestScheduler()
    private val invokeScheduler = TestScheduler()

    private val serviceFuture = BehaviorSubject.create<CitySearchResults>()

    private var transitionCommandInvocationData = argumentCaptor<CitySearchResults>()

    private var observedAppTitleText: String? = null

    private var searchServiceThatReceivedRequest: CitySearchService? = null

    private val searchService = mock<CitySearchService> {

        on { citySearch() }.thenAnswer { invocation ->

            this@StartupModelSteps.searchServiceThatReceivedRequest = invocation.mock as CitySearchService
            this@StartupModelSteps.serviceFuture
        }
    }

    fun transitionStartInterval(): Duration {

        return Duration.ofSeconds(4)
    }

    fun transitionIsScheduled(model: StartupModelImp) {

        model.startTransitionTimer()
    }

    fun transitionCommand(): StartupTransitionCommand {

        return transitionCommand
    }

    fun invokeScheduler(): Scheduler {

        return invokeScheduler
    }

    fun appTitle(): String {

        return StartupScreenTestConstants.appTitle
    }

    fun modelIsCreated(transitionCommand: StartupTransitionCommand = this.transitionCommand, searchService: CitySearchService = this.searchService): StartupModelImp {

        return StartupModelImp(transitionCommand, searchService, observableFactory, workScheduler, invokeScheduler)
    }

    fun observeAppTitleText(model: StartupModelImp) {

        model.appTitle.subscribe { appTitle ->

            this.observedAppTitleText = appTitle
        }
    }

    fun transitionTimerFires() {

        timer.onNext(0)
    }

    fun searchService(): CitySearchService {

        return searchService
    }

    fun initialResults(searchService: CitySearchService): CitySearchResults {

        return initialResults
    }

    fun searchServiceHasReturnedResults(service: CitySearchService) {

        serviceFuture.onNext(initialResults)
    }

    fun appTitleTextIsUpdated(expectedText: String) {

        Assert.assertEquals("App title text was not updated immediately", expectedText, observedAppTitleText)
    }

    fun timerIsScheduled(expectedInterval: Duration) {

        verify(observableFactory).timer(expectedInterval.seconds, TimeUnit.SECONDS, workScheduler)
    }

    fun transitionCommand(transitionCommand: StartupTransitionCommand, expectedResults: CitySearchResults, expectedQueue: Scheduler) {

        verify(transitionCommand, times(0)).invoke(any())
        workScheduler.triggerActions()
        invokeScheduler.triggerActions()
        verify(transitionCommand).invoke(transitionCommandInvocationData.capture())
        Assert.assertEquals("Transition command was not invoked with correct results", expectedResults, transitionCommandInvocationData.firstValue)
    }

    fun transitionCommandIsNotInvoked(transitionCommand: StartupTransitionCommand) {

        verify(transitionCommand, times(0)).invoke(any())
    }

    fun searchServiceResultsWereRequested(searchService: CitySearchService) {

        Assert.assertTrue("Search service did not receive initial results request", searchServiceThatReceivedRequest === searchService)
    }
}