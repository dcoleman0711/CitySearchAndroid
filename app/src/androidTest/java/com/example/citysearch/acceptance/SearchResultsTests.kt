package com.example.citysearch.acceptance

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.reactive.RecyclerCell
import com.example.citysearch.reactive.RecyclerViewBindingAdapter
import com.example.citysearch.search.OpenDetailsCommand
import com.example.citysearch.search.OpenDetailsCommandFactory
import com.example.citysearch.search.searchresults.SearchResultsModelImp
import com.example.citysearch.search.searchresults.SearchResultsViewImp
import com.example.citysearch.search.searchresults.SearchResultsViewModelImp
import com.example.citysearch.search.searchresults.citysearchresultcell.*
import com.example.citysearch.stub.CitySearchResultsStub
import com.example.citysearch.utilities.MeasureConverterImp
import com.example.citysearch.utilities.Size
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchResultsTests {

    lateinit var steps: SearchResultsSteps

    val Given: SearchResultsSteps get() = steps
    val When: SearchResultsSteps get() = steps
    val Then: SearchResultsSteps get() = steps

    @Before
    fun setUp() {
        
        steps = SearchResultsSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testClearBackground() {

        Given.createSearchResults()

        val searchResultsView = When.searchScreenIsLoaded()
        
        Then.searchResultsViewHasClearBackground(searchResultsView)
    }

    @Test
    fun testFlowLayout() {

        Given.createSearchResults()

        val searchResultsView = When.searchScreenIsLoaded()

        Then.searchResultsViewHasHorizontallyScrollingFlowLayout(searchResultsView)
    }

    @Test
    fun testInitialSearchResultsData() {

        val searchResults = Given.searchResults()
        val searchResultCells = Given.searchResultCells(searchResults)
        Given.createSearchResults(searchResults)

        val searchResultsView = When.searchScreenIsLoaded()

        Then.searchResultsCellsAreDisplayedIn(searchResultCells, searchResultsView)
    }

    @Test
    fun testInitialSearchResultsSizes() {

        val searchResults = Given.searchResults()
        val expectedCellSize = Given.searchResultCellSize()
        When.createSearchResults(searchResults)

        val searchResultsView = When.searchScreenIsLoaded()

        Then.displayedCellsAllHaveSize(searchResultsView, expectedCellSize)
    }

    @Test
    fun testTapCellToOpenDetails() {

        val searchResults = Given.searchResults()
        val searchResult = Given.searchResult(searchResults)
        val openDetailsCommand = Given.openDetailsCommand(searchResult)
        Given.createSearchResults(searchResults)
        val searchResultsView = Given.searchScreenIsLoaded()

        When.cellIsTapped(searchResult, searchResultsView)

        Then.openDetailsCommandIsInvoked(openDetailsCommand)
    }
}

class SearchResultsSteps(private val context: Context) {

    private val recyclerView = LayoutInflater.from(context).inflate(R.layout.searchresults, null) as RecyclerView

    private val resultModelFactory = mock<CitySearchResultModelFactory> {  }
    private val resultViewModelFactory = mock<CitySearchResultViewModelFactory> {  }

    private val openDetailsCommands = HashMap<String, OpenDetailsCommand>()
    private val openDetailsCommandFactory = mock<OpenDetailsCommandFactory> {

        on { openDetailsCommand(any()) }.then { invocation ->

            val searchResult = invocation.getArgument<CitySearchResult>(0)
            openDetailsCommands[searchResult.name] ?: {

                val openDetailsCommand = mock<OpenDetailsCommand>()
                whenever(openDetailsCommand.invoke()).then {

                    this@SearchResultsSteps.invokedOpenDetailsCommand = openDetailsCommand

                    null
                }

                openDetailsCommands[searchResult.name] = openDetailsCommand
                openDetailsCommand
            }()
        }
    }

    private var displayedResults = ArrayList<CitySearchResultCell>()

    private var invokedOpenDetailsCommand: OpenDetailsCommand? = null

    private lateinit var buildSearchResults: () -> SearchResultsViewImp
    private lateinit var searchResults: SearchResultsViewImp

    init {

        val modelsMap = HashMap<String, CitySearchResultModel>()
        whenever(resultModelFactory.resultModel(any(), any())).then { invocation ->

            val searchResult = invocation.getArgument<CitySearchResult>(0)
            modelsMap[searchResult.name] ?: {

                val result = mock<CitySearchResultModel>()
                val openDetailsCommand = openDetailsCommandFactory.openDetailsCommand(searchResult)
                whenever(result.openDetailsCommand).thenReturn(openDetailsCommand)
                modelsMap[searchResult.name] = result
                result
            }()
        }

        val viewModelsMap = HashMap<Int, CitySearchResultViewModel>()
        whenever(resultViewModelFactory.resultViewModel(any(), any())).then { invocation ->

            val model = invocation.getArgument<CitySearchResultModel>(1)
            val modelID = System.identityHashCode(model)
            viewModelsMap[modelID] ?: {

                val result = mock<CitySearchResultViewModel> {

                    on { title }.thenReturn(Observable.never())
                    on { iconImage }.thenReturn(Observable.never())
                }

                val openDetailsCommand = model.openDetailsCommand
                whenever(result.openDetailsCommand).thenReturn(openDetailsCommand)
                viewModelsMap[modelID] = result
                result
            }()
        }
    }

    fun searchResults(): CitySearchResults {

        return CitySearchResultsStub.stubResults()
    }

    fun searchResultCellSize(): Size {

        return Size(128, 128)
    }

    fun searchResultCells(searchResults: CitySearchResults): List<CitySearchResultCell> {

        return searchResults.results.map({ searchResult ->

            val model = resultModelFactory.resultModel(searchResult, openDetailsCommandFactory)
            val viewModel = resultViewModelFactory.resultViewModel(context, model)
            val view = LayoutInflater.from(context).inflate(R.layout.citysearchresultcell, null)
            val cell = CitySearchResultCell(view)
            cell.viewModel = viewModel
            cell
        })
    }

    fun createSearchResults(initialData: CitySearchResults = CitySearchResults.emptyResults()) {

        buildSearchResults = {

            val model = SearchResultsModelImp(resultModelFactory, openDetailsCommandFactory)
            val viewModel = SearchResultsViewModelImp(context, model, resultViewModelFactory)
            model.setResults(initialData)

            SearchResultsViewImp(context, recyclerView, viewModel)
        }
    }

    fun searchScreenIsLoaded(): SearchResultsViewImp {

        searchResults = buildSearchResults()

        RecyclerViewTestUtilities.captureDisplayedData(context, recyclerView, displayedResults)

        return searchResults
    }

    fun searchResult(searchResults: CitySearchResults): CitySearchResult {

        return searchResults.results[searchResults.results.size / 2]
    }

    fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand {

        return openDetailsCommandFactory.openDetailsCommand(searchResult)
    }

    fun cellIsTapped(result: CitySearchResult, searchResultsView: SearchResultsViewImp) {

        val model = resultModelFactory.resultModel(result, openDetailsCommandFactory)
        val viewModel = resultViewModelFactory.resultViewModel(context, model)

        val cell = displayedResults.first { cell ->

            cell.viewModel === viewModel
        }

        cell.view.performClick()
    }

    fun searchResultsCellsAreDisplayedIn(expectedCells: List<CitySearchResultCell>, searchResultsView: SearchResultsViewImp) {

        if(expectedCells.size != displayedResults.size) {
            Assert.fail("Number of displayed cells is not correct")
            return
        }

        for(index in 0 until expectedCells.size) {

            val expectedViewModel = expectedCells[index].viewModel
            val viewModel = displayedResults[index].viewModel

            Assert.assertTrue("Displayed cell is not the expected cell", viewModel === expectedViewModel)
        }
    }

    fun searchResultsViewHasClearBackground(searchResultsView: SearchResultsViewImp) {

        val backgroundColor = (searchResultsView.view.background as ColorDrawable).color
        Assert.assertEquals("Search Results background is not clear", Color.TRANSPARENT, backgroundColor)
    }

    fun searchResultsViewHasHorizontallyScrollingFlowLayout(searchResultsView: SearchResultsViewImp) {

        val layoutManager = recyclerView.layoutManager as? GridLayoutManager
        if(layoutManager == null) {
            Assert.fail("Recycler view is not grid layout")
            return
        }

        Assert.assertEquals("Search results scroll direction is not horizontal", GridLayoutManager.HORIZONTAL, layoutManager.orientation)
        Assert.assertEquals("Search results row count is not 2", 2, layoutManager.spanCount)
    }

    fun displayedCellsAllHaveSize(searchResultsView: SearchResultsViewImp, expectedSize: Size) {

        val measureConverter = MeasureConverterImp(context)
        val width = measureConverter.convertToPixels(expectedSize.width)
        val height = measureConverter.convertToPixels(expectedSize.height)

        for(cell in displayedResults) {

            val sizeIsCorrect =
                cell.view.layoutParams.width == width &&
                cell.view.layoutParams.height == height

            Assert.assertTrue("Displayed cells does not have the correct size", sizeIsCorrect)
        }
    }

    fun openDetailsCommandIsInvoked(detailsCommand: OpenDetailsCommand) {

        Assert.assertTrue("Open Details command was not invoked", invokedOpenDetailsCommand === detailsCommand)
    }
}

class RecyclerViewTestUtilities {

    companion object {

        fun<ViewModel, CellType: RecyclerCell<ViewModel>> captureDisplayedData(context: Context, recyclerView: RecyclerView, results: ArrayList<CellType>) {

            val adapter = recyclerView.adapter!! as RecyclerViewBindingAdapter<ViewModel, CellType>

            val updater = object: RecyclerView.AdapterDataObserver() {

                override fun onChanged() {

                    results.clear()

                    val parent = FrameLayout(context)

                    for(index in 0 until adapter.itemCount) {

                        val holder = adapter.onCreateViewHolder(parent, 0)
                        adapter.onBindViewHolder(holder, index)
                        results.add(holder.cell as CellType)
                    }
                }
            }

            adapter.registerAdapterDataObserver(updater)

            updater.onChanged()
        }
    }
}