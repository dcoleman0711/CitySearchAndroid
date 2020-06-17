package com.example.citysearch.unit

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.StubMeasureConverter
import com.example.citysearch.parallax.ParallaxView
import com.example.citysearch.search.SearchModel
import com.example.citysearch.search.SearchView
import com.example.citysearch.search.SearchViewModel
import com.example.citysearch.search.searchresults.SearchResultsModel
import com.example.citysearch.search.searchresults.SearchResultsView
import com.example.citysearch.utilities.ConstraintSetFactory
import com.example.citysearch.utilities.MeasureConverter
import com.example.citysearch.utilities.ViewUtilities
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

class SearchScreenTestConstants {

    companion object {

        const val resultsHeight = 312
    }
}

@RunWith(AndroidJUnit4::class)
class SearchViewTests {

    lateinit var steps: SearchViewSteps

    val Given: SearchViewSteps get() = steps
    val When: SearchViewSteps get() = steps
    val Then: SearchViewSteps get() = steps

    @Before
    fun setUp() {

        steps = SearchViewSteps()
    }

    @Test
    fun testParallaxViewFillsHorizontallyAndIsCenteredVerticallyInParentSafeArea() {

        val parallaxView = Given.parallaxView()
        val expectedConstraints = Given.constraintsToFillParent(parallaxView.view)

        val searchView = When.searchViewIsCreated(parallaxView = parallaxView)

        Then.searchViewHasExpectedConstraints(searchView, expectedConstraints)
    }

    @Test
    fun testSearchResultsViewIsOnSearchView() {

        val searchResultsView = Given.searchResultsView()

        val searchView = When.searchViewIsCreated(searchResultsView = searchResultsView)

        Then.viewIsOnSearchView(searchResultsView.view, searchView)
    }

    @Test
    fun testSearchResultsViewFillsHorizontallyAndIsCenteredVerticallyInParentSafeArea() {

        val searchResultsView = Given.searchResultsView()
        val expectedConstraints = Given.constraintsToFillHorizontallyAndCenterVerticallyInParent(searchResultsView.view)

        val searchView = When.searchViewIsCreated(searchResultsView = searchResultsView)

        Then.searchViewHasExpectedConstraints(searchView, expectedConstraints)
    }
}

class SearchViewSteps {

    private val view = mock<ConstraintLayout> {  }

    private val measureConverter = StubMeasureConverter.stubMeasureConverter()

    private val searchResultsView = mock<SearchResultsView> {

        on { view }.thenReturn(mock())
    }

    private val parallaxView = mock<ParallaxView> {

        on { view }.thenReturn(mock())
    }

    private val viewModel = mock<SearchViewModel> {  }

    private val constraints = mock<ConstraintSet> {  }

    private val constraintSetFactory = mock<ConstraintSetFactory> {

        on { constraintSet() }.thenReturn(constraints)
    }

    fun searchResultsView(): SearchResultsView {

        return searchResultsView
    }

    fun parallaxView(): ParallaxView {

        return parallaxView
    }

    fun constraintsToFillHorizontallyAndCenterVerticallyInParent(view: View): () -> Unit {

        return {

            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            verify(constraints, atLeastOnce()).centerVertically(view.id, ConstraintSet.PARENT_ID)
            verify(constraints, atLeastOnce()).constrainHeight(view.id, View.MeasureSpec.makeMeasureSpec(measureConverter.convertToPixels(SearchScreenTestConstants.resultsHeight), View.MeasureSpec.EXACTLY))
        }
    }

    fun constraintsToFillParent(view: View): () -> Unit {

        return {

            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    fun searchViewIsCreated(view: ConstraintLayout = this.view, viewModel: SearchViewModel = this.viewModel, searchResultsView: SearchResultsView = this.searchResultsView, parallaxView: ParallaxView = this.parallaxView): SearchView {

        return SearchView(view, viewModel, searchResultsView, parallaxView, constraintSetFactory, measureConverter)
    }

    fun viewIsOnSearchView(view: View, searchView: SearchView) {

        verify(this.view).addView(view)
    }

    fun searchViewHasExpectedConstraints(searchView: SearchView, expectedConstraints: () -> Unit) {

        expectedConstraints()
    }
}