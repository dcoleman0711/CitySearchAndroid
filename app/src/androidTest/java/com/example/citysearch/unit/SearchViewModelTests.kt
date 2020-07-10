package com.example.citysearch.unit

import android.graphics.Point
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.parallax.ParallaxViewModel
import com.example.citysearch.search.SearchModel
import com.example.citysearch.search.SearchViewModelImp
import com.example.citysearch.search.searchresults.SearchResultsViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchViewModelTests {

    lateinit var steps: SearchViewModelSteps

    val Given: SearchViewModelSteps get() = steps
    val When: SearchViewModelSteps get() = steps
    val Then: SearchViewModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = SearchViewModelSteps()
    }

    @Test
    fun testConnectParallaxToSearchResults() {

        val parallaxViewModel = Given.parallaxViewModel()
        val searchResultsViewModel = Given.searchResultsViewModel()

        val searchViewModel = When.searchViewModelIsCreated(parallaxViewModel = parallaxViewModel, searchResultsViewModel = searchResultsViewModel)

        Then.parallaxViewModelReceivesSearchResultsOffsets(parallaxViewModel, searchResultsViewModel)
    }
}

class SearchViewModelSteps {

    private val parallaxViewModel = mock<ParallaxViewModel> {  }

    private val searchResultsOffset = BehaviorSubject.create<Point>()
    private val searchResultsViewModel = mock<SearchResultsViewModel> {

        on { contentOffset }.thenReturn(searchResultsOffset)
    }

    private val model = mock<SearchModel> {  }

    fun parallaxViewModel(): ParallaxViewModel {

        return parallaxViewModel
    }

    fun searchResultsViewModel(): SearchResultsViewModel {

        return searchResultsViewModel
    }

    fun searchViewModelIsCreated(parallaxViewModel: ParallaxViewModel, searchResultsViewModel: SearchResultsViewModel): SearchViewModelImp {

        return SearchViewModelImp(model, parallaxViewModel, searchResultsViewModel)
    }

    fun parallaxViewModelReceivesSearchResultsOffsets(parallaxViewModel: ParallaxViewModel, searchResultsViewModel: SearchResultsViewModel) {

        verify(parallaxViewModel).contentOffset = searchResultsOffset
    }
}