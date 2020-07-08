package com.example.citysearch.acceptance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.details.CityDetailsFragment
import com.example.citysearch.details.CityDetailsView
import com.example.citysearch.details.CityDetailsFragmentFactory
import com.example.citysearch.parallax.ParallaxView
import com.example.citysearch.parallax.ParallaxViewImp
import com.example.citysearch.parallax.ParallaxViewModel
import com.example.citysearch.search.*
import com.example.citysearch.search.searchresults.*
import com.example.citysearch.stub.CitySearchResultsStub
import com.example.citysearch.utilities.*
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.collections.HashMap

class SearchScreenTestConstants {

    companion object {

        const val resultsHeight = 312
    }
}

@RunWith(AndroidJUnit4::class)
class SearchScreenTests {

    lateinit var steps: SearchScreenSteps

    val Given: SearchScreenSteps get() = steps
    val When: SearchScreenSteps get() = steps
    val Then: SearchScreenSteps get() = steps

    @Before
    fun setUp() {
        
        steps = SearchScreenSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testSearchResultsDisplayed() {

        val searchResultsView = Given.searchResultsView()
        Given.searchScreen(searchResultsView = searchResultsView)

        val searchView = When.searchScreenIsLoaded()

        Then.searchResultsIsDisplayedIn(searchResultsView, searchView)
    }

    @Test
    fun testParallaxFullScreen() {

        val parallaxView = Given.parallaxView()
        Given.searchScreen(parallaxView = parallaxView)

        val searchScreen = When.searchScreenIsLoaded()

        Then.parallaxViewIsFullScreenIn(parallaxView, searchScreen)
    }

    @Test
    fun testParallaxOffset() {

        val parallaxView = Given.parallaxView()
        val searchResultsView = Given.searchResultsView()
        Given.searchScreen(parallaxView = parallaxView, searchResultsView = searchResultsView)
        val searchScreen = Given.searchScreenIsLoaded()
        val contentOffset = Given.contentOffset()

        When.searchResultsScrollsTo(contentOffset)

        Then.parallaxViewOffsetIsBoundToSearchResults(parallaxView, contentOffset)
    }

    @Test
    fun testSearchResultsFullWidthAndCenteredVerticallyInSafeArea() {

        val searchResultsView = Given.searchResultsView()
        Given.searchScreen(searchResultsView = searchResultsView)

        val searchView = When.searchScreenIsLoaded()

        Then.searchResultsFillsHorizontallyAndIsCenteredVerticallyInSafeArea(searchView, searchResultsView)
    }

    @Test
    fun testOpenDetailsCommand() {

        val initialData = Given.initialData()
        val searchResultsView = Given.searchResultsView()
        val searchResult = Given.searchResult(initialData)
        Given.searchScreen(searchResultsView = searchResultsView, initialData = initialData)
        val searchView = Given.searchScreenIsLoaded()
        val openDetailsCommand = Given.openDetailsCommand(searchResult)

        When.invoke(openDetailsCommand)

        Then.detailsScreenIsPushedOntoNavigationStack(searchResult)
    }
}

class SearchScreenSteps(private val context: Context) {

    private val fragmentTransaction = mock<FragmentTransaction> {

    }

    private val fragmentManager = mock<FragmentManager> {

        on { beginTransaction() }.thenReturn(fragmentTransaction)
    }

    private val parallaxViewModel = mock<ParallaxViewModel> {

        on { images }.thenReturn(BehaviorSubject.create())
        on { offsets }.thenReturn(BehaviorSubject.create())
    }

    private val searchResultsContentOffset = BehaviorSubject.create<Point>()

    private val detailsFragments = HashMap<String, CityDetailsFragment>()
    private val detailsFragmentFactory = mock<CityDetailsFragmentFactory> {

        on { detailsFragment(any()) }.then { invocation ->

            val searchResult = invocation.getArgument<CitySearchResult>(0)
            val detailsFragment = mock<CityDetailsFragment>()
            detailsFragments[searchResult.name] = detailsFragment

            detailsFragment
        }
    }

    private var openDetailsCommands = HashMap<String, OpenDetailsCommand>()
    private val realOpenDetailsCommandFactory = OpenDetailsCommandFactoryImp(fragmentManager, detailsFragmentFactory)
    private val openDetailsCommandFactory = mock<OpenDetailsCommandFactory> {

        on { openDetailsCommand(any()) }.then { invocation ->

            val searchResult = invocation.getArgument<CitySearchResult>(0)
            val openDetailsCommand = realOpenDetailsCommandFactory.openDetailsCommand(searchResult)
            openDetailsCommands[searchResult.name] = openDetailsCommand

            openDetailsCommand
        }
    }

    private val searchResultsModel = SearchResultsModelImp(openDetailsCommandFactory)
    private val searchResultsViewModel = SearchResultsViewModelImp(context, searchResultsModel)

    private val searchView = LayoutInflater.from(context).inflate(R.layout.search, null) as ConstraintLayout

    private val searchResultsView = SearchResultsViewImp(context, searchView.findViewById(R.id.searchResults), searchResultsViewModel)
    private val parallaxView = ParallaxViewImp(context, searchView.findViewById(R.id.parallaxView), parallaxViewModel)

    private var parallaxOffset: Point? = null
    private var parallaxSubscriber: Disposable? = null

    private lateinit var buildSearchScreen: () -> SearchView
    private lateinit var searchScreen: SearchView

    val contentOffsetCaptor = argumentCaptor<Observable<Point>>()

    init {

        whenever(fragmentTransaction.replace(any(), any())).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.addToBackStack(anyOrNull())).thenReturn(fragmentTransaction)

        searchResultsViewModel.provideContentOffset(searchResultsContentOffset)

        doNothing().whenever(parallaxViewModel).contentOffset = contentOffsetCaptor.capture()
    }

    fun initialData(): CitySearchResults {

        return CitySearchResultsStub.stubResults()
    }

    fun contentOffset(): Point {

        return Point(10, 0)
    }

    fun searchResultsView(): SearchResultsView {

        return searchResultsView
    }

    fun parallaxView(): ParallaxView {

        return parallaxView
    }

    fun searchScreen(parallaxView: ParallaxView = this.parallaxView, searchResultsView: SearchResultsView = this.searchResultsView, initialData: CitySearchResults = CitySearchResults.emptyResults()) {

        searchResultsModel.setResults(initialData)

        val searchModel = mock<SearchModel>()
        val searchViewModel = SearchViewModelImp(searchModel, parallaxViewModel, searchResultsViewModel)

        this.buildSearchScreen = {

            SearchViewImp(searchView,
                searchViewModel,
                searchResultsView,
                parallaxView)
        }
    }

    fun searchScreenIsLoaded(): SearchView {

        searchScreen = buildSearchScreen()

        return searchScreen
    }

    fun searchResultsScrollsTo(contentOffset: Point) {

        parallaxSubscriber = contentOffsetCaptor.firstValue.subscribe { contentOffset ->

            parallaxOffset = contentOffset
        }

        searchResultsContentOffset.onNext(contentOffset)
    }

    fun searchResult(searchResults: CitySearchResults): CitySearchResult {

        return searchResults.results[searchResults.results.size / 2]
    }

    fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand {

        return openDetailsCommands[searchResult.name]!!
    }

    fun invoke(openDetailsCommand: OpenDetailsCommand) {

        openDetailsCommand.invoke()
    }

    fun parallaxViewOffsetIsBoundToSearchResults(parallaxView: ParallaxView, contentOffset: Point) {

        Assert.assertEquals("Parallax offset is not bound to Search Sesults", contentOffset, parallaxOffset)
    }

    fun searchResultsIsDisplayedIn(searchResults: SearchResultsView, searchScreen: SearchView) {

        Assert.assertTrue("Search results are not displayed in search view", ViewUtilities.isDescendantOf(searchResults.view, searchScreen.view))
    }

    fun searchResultsFillsHorizontallyAndIsCenteredVerticallyInSafeArea(searchScreen: SearchView, searchResults: SearchResultsView) {

        val searchView = searchScreen.view

        searchView.measure(View.MeasureSpec.makeMeasureSpec(2048, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(1536, View.MeasureSpec.EXACTLY))
        searchView.layout(0, 0, 2048, 1536)

        val fillsHorizontally =
                searchResults.view.left == 0 &&
                searchResults.view.right == searchView.width

        val centeredVertically = (searchResults.view.top + searchResults.view.bottom) / 2 == searchView.height / 2

        val correctHeight = searchResults.view.height == MeasureConverterImp(context).convertToPixels(SearchScreenTestConstants.resultsHeight)

        Assert.assertTrue("Search results do not fill view horizontally", fillsHorizontally)
        Assert.assertTrue("Search results are not centered vertically", centeredVertically)
        Assert.assertTrue("Search results does not have correct height", correctHeight)
    }

    fun parallaxViewIsFullScreenIn(parallaxView: ParallaxView, searchScreen: SearchView) {

        val searchView = searchScreen.view

        val fillsView =
            parallaxView.view.left == 0 && parallaxView.view.right == searchView.width &&
            parallaxView.view.top == 0 && parallaxView.view.bottom == searchView.height

        Assert.assertTrue("Parallax view is not full screen in search screen", fillsView)
    }

    fun detailsScreenIsPushedOntoNavigationStack(searchResult: CitySearchResult) {

        val detailsScreen = detailsFragments[searchResult.name]
        if(detailsScreen == null) {
            Assert.fail("Details View for search result was not created")
            return
        }

        verify(fragmentManager).beginTransaction()

        verify(fragmentTransaction).replace(android.R.id.content, detailsScreen)
        verify(fragmentTransaction).addToBackStack(anyOrNull())
        verify(fragmentTransaction).commit()
    }
}