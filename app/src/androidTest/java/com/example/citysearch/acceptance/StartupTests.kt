//package com.example.citysearch.acceptance
//
//import android.app.Activity
//import androidx.test.core.app.launchActivity
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.citysearch.data.CitySearchService
//import com.example.citysearch.startup.StartupView
//import org.junit.Before
//import org.junit.Test
//
//import org.junit.runner.RunWith
//import org.mockito.Mockito.mock
//import java.time.Duration
//
//class StartupConstants {
//
//    companion object {
//
//        val transitionDuration = Duration.ofSeconds(1)
//        val transitionType = android.R.anim.slide_in_left;
//    }
//}
//
//@RunWith(AndroidJUnit4::class)
//class StartupTests {
//
//    lateinit var steps: StartupSteps
//
//    val Given: StartupSteps get() = steps
//    val When: StartupSteps get() = steps
//    val Then: StartupSteps get() = steps
//
//    @Before
//    fun setUp() {
//
//        steps = StartupSteps()
//    }
//
//    @Test
//    fun testTransitionToSearchView() {
//
//        val initialData = Given.initialData()
//        val startupView = Given.startupView()
//        val searchView = Given.searchView()
//        val transitionType = Given.transitionType()
//        val duration = Given.transitionDuration()
//
//        When.transitionToSearchViewBegins(initialData)
//
//        Then.transitionIsAppliedToNewView(transitionType, startupView, searchView, duration)
//    }
//
//    @Test
//    fun testTransitionToSearchViewInitialData() {
//
//        val initialData = Given.initialData()
//        val searchView = Given.searchView()
//
//        When.transitionToSearchViewBegins(initialData)
//
//        Then.searchViewInitialDataIs(searchView, initialData)
//    }
//}
//
//class StartupSteps {
//
//    private var startupViewScenario = launchActivity<StartupView>()
//    private lateinit var startupView: StartupView
////    private val searchViewStub = launch SearchViewFactoryImp().searchView(initialData: CitySearchResults.emptyResults())
//
//    private val searchService = mock(CitySearchService::class.java)
//
//    private val transitionCommandFactory = StartupTransitionCommandFactoryMock()
//    private var transitionCommand: StartupTransitionCommand? = null
//
//    private var durationUsedInAnimation: Duration? = null
//    private var transitionTypeUsedInAnimation: Int? = null
//    private var transitionOldView: Activity? = null
//    private var transitionNewView: Activity? = null
//
//    private var searchViewInitialData: CitySearchResults? = null
//
//    init {
//
//        startupViewScenario.onActivity { activity -> this.startupView = activity }
////
////        searchViewFactory.searchViewImp = { (initialData) in
////
////                this.searchViewInitialData = initialData
////
////            return this.searchViewStub
////        }
////
////        transitionCommandFactory.startupTransitionCommandImp = { (window, newRoot, viewType) in
////
////            val transitionCommand = StartupTransitionCommandImp(window: window, searchViewFactory: newRoot, viewType: UIViewMock.this)
////            this.transitionCommand = transitionCommand
////            return transitionCommand
////        }
//    }
//
//    fun startupView(): StartupView {
//
//        return startupView
//    }
//
//    fun searchView(): SearchView {
//
//        return searchViewStub
//    }
//
//    fun transitionType(): Int {
//
//        return StartupConstants.transitionType
//    }
//
//    fun transitionDuration(): Duration {
//
//        return StartupConstants.transitionDuration
//    }
//
//    fun initialData(): CitySearchResults {
//
//        val initialData = CitySearchResultsStub.stubResults()
//
//        val future = CitySearchService.SearchFuture({ promise in
//
//                promise(.success(initialData))
//        })
//
//        searchService.citySearchImp = { future }
//
//        return initialData
//    }
//
//    fun appIsLaunched(app: AppDelegate) {
//
//        app.applicationDidFinishLaunching(UIApplication.shared)
//    }
//
//    fun transitionToSearchViewBegins(initialResults: CitySearchResults) {
//
//        transitionCommand?.invoke(initialResults: initialResults)
//    }
//
//    fun startupScreenAppearsFullScreen(app: AppDelegate) {
//
//        val window = app.window
//        if(window == null){
//            fail("AppDelegate window is nil")
//            return
//        }
//
//        assertTrue(window.isKeyWindow, "AppDelegate window is not key window")
//
//        guard val rootController = window.rootViewController else {
//            XCTFail("Application window does not have root view controller after launch")
//            return
//        }
//
//        XCTAssertTrue(rootController is StartupView, "Root controller's is not Startup Screen")
//    }
//
//    fun transitionIsAppliedToNewView(expectedTransitionType: Int, expectedOldView: UIViewController, expectedNewView: UIViewController, expectedDuration: TimeInterval) {
//
//        XCTAssertEqual(durationUsedInAnimation, expectedDuration)
//        XCTAssertEqual(transitionTypeUsedInAnimation, expectedTransitionType)
//        XCTAssertEqual(transitionOldView, expectedOldView)
//
//        guard val navigationController = transitionNewView as? UINavigationController else {
//            XCTFail("Transition destination is not navigation stack")
//            return
//        }
//
//        XCTAssertEqual(navigationController.viewControllers, [expectedNewView], "Navigation stack is not Search View")
//    }
//
//    fun searchViewInitialDataIs(view: SearchView, expectedInitialData: CitySearchResults) {
//
//        XCTAssertEqual(searchViewInitialData, expectedInitialData)
//    }
//}