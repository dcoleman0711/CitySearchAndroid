package com.example.citysearch.acceptance

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import java.time.Duration

class StartupConstants {

    companion object {

        val transitionDuration = Duration.ofSeconds(1)
        val transitionType = android.R.anim.slide_in_left;
    }
}

@RunWith(AndroidJUnit4::class)
class StartupTests {

    lateinit var steps: StartupSteps

    val Given: StartupSteps get() = steps
    val When: StartupSteps get() = steps
    val Then: StartupSteps get() = steps

    @Before
    fun setUp() {

        steps = StartupSteps()
    }

//    @Test
//    fun testTransitionToSearchView() {
//
//        val initialData = Given.initialData()
//        val startupView = Given.startupView()
//        val searchView = Given.searchViewInitialDataIs()
//        val transitionType = Given.transitionType()
//        val duration = Given.transitionDuration()
//        val app = Given.application()
//        Given.appIsLaunched(app: app)
//
//        When.transitionToSearchViewBegins(with: initialData)
//
//        Then.transitionIsAppliedToNavigationStackWithNewView(ofType: transitionType, isAppliedFrom: startupView, toNavigationStackContaining: searchView, with: duration)
//    }

//    @Test
//    fun testTransitionToSearchViewInitialData() {
//
//        val initialData = Given.initialData()
//        val searchView = Given.searchViewInitialDataIs()
//        val app = Given.application()
//        Given.appIsLaunched(app: app)
//
//        When.transitionToSearchViewBegins(with: initialData)
//
//        Then.searchViewInitialDataIs(searchView, initialDataIs: initialData)
//    }
}

class StartupSteps {

//    private var startupViewStub = StartupViewBuilderImp().build()
//
//    private val searchViewFactory = SearchViewFactoryMock()
//    private val searchViewStub = SearchViewFactoryImp().searchView(initialData: CitySearchResults.emptyResults())
//
//    private val searchService = CitySearchServiceMock()
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

    init {

//        searchViewFactory.searchViewImp = { (initialData) in
//
//                this.searchViewInitialData = initialData
//
//            return this.searchViewStub
//        }
//
//        transitionCommandFactory.startupTransitionCommandImp = { (window, newRoot, viewType) in
//
//            val transitionCommand = StartupTransitionCommandImp(window: window, searchViewFactory: newRoot, viewType: UIViewMock.this)
//            this.transitionCommand = transitionCommand
//            return transitionCommand
//        }
    }

//    fun application(): AppDelegate {
//
////        val startupViewBuilder = StartupViewBuilderMock()
////
////        startupViewBuilder.buildImp = { this.startupViewStub }
//
//        val app = AppDelegate(startupViewBuilder: startupViewBuilder, searchViewFactory: searchViewFactory, searchService: searchService, transitionCommandFactory: transitionCommandFactory)
//
////        UIViewMock.transitionImp = { (view, duration, options, animations, compvalion) in
////
////            if view == app.window {
////
////                this.durationUsedInAnimation = duration
////                this.transitionTypeUsedInAnimation = options.intersection([UIView.AnimationOptions.transitionFlipFromRight])
////
////                this.transitionOldView = app.window?.rootViewController
////                animations?()
////                this.transitionNewView = app.window?.rootViewController
////            }
////        }
//
//        return app
//    }

//    fun startupView(): StartupView {
//
//        return startupViewStub
//    }
//
//    fun searchViewInitialDataIs(): SearchView {
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

//    fun transitionIsAppliedToNavigationStackWithNewView(expectedTransitionType: Int, expectedOldView: UIViewController, expectedNewView: UIViewController, expectedDuration: TimeInterval) {
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
}