package com.example.citysearch.unit

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.reactive.CellData
import com.example.citysearch.reactive.RecyclerViewModel
import com.example.citysearch.search.OpenDetailsCommand
import com.example.citysearch.search.searchresults.SearchResultsModel
import com.example.citysearch.search.searchresults.SearchResultsViewModelImp
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModelFactory
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.Size
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchResultsViewModelTests {

    lateinit var steps: SearchResultsViewModelSteps

    val Given: SearchResultsViewModelSteps get() = steps
    val When: SearchResultsViewModelSteps get() = steps
    val Then: SearchResultsViewModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = SearchResultsViewModelSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testObserveSearchResultsUpdate() {

        val model = Given.searchResultsModel()
        val viewModel = Given.searchResultsViewModelCreated(model = model)
        val resultModels = Given.resultModels()
        val resultViewModels = Given.resultViewModels(resultModels)
        val cellSize = Given.cellSize()
        val resultsData = Given.resultsData(resultViewModels, cellSize)
        val observer = Given.observeSearchResults(viewModel)

        When.modelUpdatesResults(model, resultModels)

        Then.observerIsNotifiedWithResults(resultsData)
    }
}

class SearchResultsViewModelSteps(private val context: Context) {

    private var updatedContentOffset: Point? = null

    private val resultViewModelFactory = mock<CitySearchResultViewModelFactory> {  }

    private val modelResults = BehaviorSubject.create<List<CitySearchResultModel>>()
    private val searchResultsModel = mock<SearchResultsModel> {

        on { resultsModels }.thenReturn(modelResults)
    }

    private var valuePassedToObserver: RecyclerViewModel<CitySearchResultViewModel>? = null

    private var updatedResultModels: List<CitySearchResultModel> = arrayListOf()


    fun resultViewModels(resultModels: List<CitySearchResultModel>): List<CitySearchResultViewModel> {

        val viewModelsMap = HashMap<Int, CitySearchResultViewModel>()
        whenever(resultViewModelFactory.resultViewModel(any(), any())).then { invocation ->

            val model = invocation.getArgument<CitySearchResultModel>(1)
            val modelID = System.identityHashCode(model)
            viewModelsMap[modelID] ?: {

                val result = mock<CitySearchResultViewModel>()
                val openDetailsCommand = mock<OpenDetailsCommand>()
                whenever(result.openDetailsCommand).thenReturn(openDetailsCommand)
                viewModelsMap[modelID] = result
                result
            }()
        }

        return resultModels.map( { resultModel -> resultViewModelFactory.resultViewModel(context, resultModel) } )
    }

    fun searchResultsViewModelCreated(model: SearchResultsModel): SearchResultsViewModelImp {

        return SearchResultsViewModelImp(context, model, resultViewModelFactory)
    }

    fun searchResultsModel(): SearchResultsModel {

        return searchResultsModel
    }

    fun modelUpdatesResults(model: SearchResultsModel, resultModels: List<CitySearchResultModel>) {

        modelResults.onNext(resultModels)
    }

    fun resultModels(): List<CitySearchResultModel> {

        return (0 until 5).map({ mock<CitySearchResultModel>() })
    }

    fun cellSize(): Size {

        return Size(128, 128)
    }

    fun resultsData(viewModels: List<CitySearchResultViewModel>, cellSize: Size): RecyclerViewModel<CitySearchResultViewModel> {

        val cells = viewModels.map({ viewModel -> CellData(viewModel, cellSize, viewModel.openDetailsCommand) })
        return RecyclerViewModel(cells, 0, 0)
    }

    fun observeSearchResults(viewModel: SearchResultsViewModelImp): Disposable {

        return viewModel.resultsViewModels.subscribe { results ->
            
            valuePassedToObserver = results
        }
    }

    fun observerIsNotifiedWithResults(expectedResults: RecyclerViewModel<CitySearchResultViewModel>) {

        Assert.assertTrue("Observer was not notified of correct results", ListCompareUtility.compareLists(expectedResults.cells, valuePassedToObserver?.cells, { first, second -> first.viewModel === second.viewModel && first.size == second.size && first.tapCommand === second.tapCommand }))
    }
}