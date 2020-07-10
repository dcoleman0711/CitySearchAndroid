package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.entities.CitySearchResults
import com.example.citysearch.factories.OpenDetailsCommandFactory
import com.example.citysearch.models.SearchResultsModelImp
import com.example.citysearch.models.CitySearchResultModel
import com.example.citysearch.factories.CitySearchResultModelFactory
import com.example.citysearch.stub.CitySearchResultsStub
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.disposables.Disposable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchResultsModelTests {

    lateinit var steps: SearchResultsModelSteps

    val Given: SearchResultsModelSteps get() = steps
    val When: SearchResultsModelSteps get() = steps
    val Then: SearchResultsModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = SearchResultsModelSteps()
    }

    @Test
    fun testSetResults() {

        val searchResults = Given.searchResults()
        val expectedResultModels = Given.models(searchResults)
        val searchModel = Given.searchResultsModelCreated()
        val searchResultsObserver = Given.observeSearchResults(searchModel)

        When.searchResultsModelDataIsSetTo(searchModel, searchResults)

        Then.observerIsNotifiedWithResults(expectedResultModels)
    }

    @Test
    fun testPassCommandFactory() {

        val searchResults = Given.searchResults()
        val tapCommandFactory = Given.tapCommandFactory()
        val searchModel = Given.searchResultsModelCreated(tapCommandFactory = tapCommandFactory)

        When.searchResultsModelDataIsSetTo(searchModel, searchResults)

        Then.resultModelsTapCommandFactoriesAreAll(tapCommandFactory)
    }
}

class SearchResultsModelSteps {

    private val resultModelFactory = mock<CitySearchResultModelFactory> {  }

    private val tapCommandFactory = mock<OpenDetailsCommandFactory> {

        on { openDetailsCommand(any()) }.thenReturn(mock())
    }

    private var valuePassedToObserver: List<CitySearchResultModel>? = null

    private var resultModelsValue: List<CitySearchResultModel> = arrayListOf()
    private var resultModelsCommandFactories = HashMap<Int, OpenDetailsCommandFactory>()

    init {

        val modelsMap = HashMap<String, CitySearchResultModel>()
        whenever(resultModelFactory.resultModel(any(), any())).then { invocation ->

            val searchResult = invocation.getArgument<CitySearchResult>(0)
            val tapCommandFactory = invocation.getArgument<OpenDetailsCommandFactory>(1)

            val model = modelsMap[searchResult.name] ?: {

                val result = mock<CitySearchResultModel>()
                modelsMap[searchResult.name] = result
                result
            }()

            resultModelsCommandFactories[System.identityHashCode(model)] = tapCommandFactory

            model
        }
    }

    fun emptyResults(): CitySearchResults {

        return CitySearchResults.emptyResults()
    }

    fun searchResults(): CitySearchResults {

        return CitySearchResultsStub.stubResults()
    }

    fun models(searchResults: CitySearchResults): List<CitySearchResultModel> {

        return searchResults.results.map( { searchResult -> resultModelFactory.resultModel(searchResult, mock()) } )
    }

    fun tapCommandFactory(): OpenDetailsCommandFactory {

        return tapCommandFactory
    }

    fun searchResultsModelCreated(tapCommandFactory: OpenDetailsCommandFactory = this.tapCommandFactory): SearchResultsModelImp {

        return SearchResultsModelImp(
            resultModelFactory,
            tapCommandFactory
        )
    }

    fun searchResultsModelDataIsSetTo(searchResultsModel: SearchResultsModelImp, searchResults: CitySearchResults) {

        searchResultsModel.setResults(searchResults)
    }

    fun observeSearchResults(model: SearchResultsModelImp): Disposable {

        return model.resultsModels.subscribe { resultModels ->

            valuePassedToObserver = resultModels
        }
    }

    fun observerIsNotifiedWithResults(expectedValue: List<CitySearchResultModel>) {

        Assert.assertTrue("Observer was not notified of correct results", ListCompareUtility.compareListsWithPointerEquality(expectedValue, valuePassedToObserver))
    }

    fun resultModelsTapCommandFactoriesAreAll(tapCommandFactory: OpenDetailsCommandFactory) {

        val resultModelsFactories = resultModelsValue.map({ resultModel -> resultModelsCommandFactories[System.identityHashCode(resultModel)] })

        Assert.assertTrue("Result models were not created with the correct tap command factory", resultModelsFactories.stream().allMatch { factory -> factory === tapCommandFactory })
    }
}