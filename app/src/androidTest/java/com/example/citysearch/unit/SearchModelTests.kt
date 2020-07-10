package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.entities.CitySearchResults
import com.example.citysearch.models.SearchResultsModel
import com.example.citysearch.stub.CitySearchResultsStub
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchModelTests {

    lateinit var steps: SearchModelSteps

    val Given: SearchModelSteps get() = steps
    val When: SearchModelSteps get() = steps
    val Then: SearchModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = SearchModelSteps()
    }

    @Test
    fun test() {

        // Empty test to prevent JUnit from failing
    }
}

class SearchModelSteps {

    private var displayedSearchResults: CitySearchResults? = null

    private val searchResultsModel = mock<SearchResultsModel> {

        on { setResults(any()) }.then { invocation ->

            displayedSearchResults = invocation.getArgument<CitySearchResults>(0)

            null
        }
    }

    fun initialData(): CitySearchResults {

        return CitySearchResultsStub.stubResults()
    }

    fun searchResultsModel(): SearchResultsModel {

        return searchResultsModel
    }

    fun searchResultsModelIsDisplayingData(searchResultsModel: SearchResultsModel, expectedData: CitySearchResults) {

        Assert.assertEquals("Search results model is not displaying expected data", expectedData, displayedSearchResults)
    }
}