package com.example.citysearch.unit

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.search.SearchFragment
import com.example.citysearch.search.SearchFragmentFactory
import com.example.citysearch.search.searchresults.SearchResultsModel
import com.example.citysearch.search.searchresults.SearchResultsModelFactory
import com.example.citysearch.startup.StartupTransitionCommandImp
import com.example.citysearch.stub.CitySearchResultsStub
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupTransitionTests {

    lateinit var steps: StartupTransitionSteps

    val Given: StartupTransitionSteps get() = steps
    val When: StartupTransitionSteps get() = steps
    val Then: StartupTransitionSteps get() = steps

    @Before
    fun setUp() {

        steps = StartupTransitionSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testTransitionTargetActivity() {

        val transitionCommand = Given.transitionCommand()

        When.transitionCommandIsInvoked(transitionCommand)

        Then.transitionTargetIsSearchView()
    }

    @Test
    fun testTransitionAnimations() {

        val transitionCommand = Given.transitionCommand()

        When.transitionCommandIsInvoked(transitionCommand)

        Then.transitionCommandIsInvokedWithAnimations()
    }


    @Test
    fun testTransitionInitialData() {

        val initialResults = Given.initialResults()
        val transitionCommand = Given.transitionCommand()

        When.transitionCommandIsInvokedWithInitialResults(transitionCommand, initialResults)

        Then.searchViewIsCreatedWithInitialResults(initialResults)
    }
}

class StartupTransitionSteps(private val context: Context) {

    private var rootFragment: Fragment? = null

    private val transaction = mock<FragmentTransaction> {  }

    private val fragmentManager = mock<FragmentManager> {

        on { beginTransaction() }.then { invocation ->

            var fragment: Fragment? = null

            whenever(transaction.replace(any(), any())).then { invocation ->

                val viewId = invocation.getArgument<Int>(0)

                if(viewId == android.R.id.content)
                    fragment = invocation.getArgument<Fragment>(1)

                transaction
            }

            whenever(transaction.commit()).then { invocation ->

                rootFragment = fragment

                null
            }

            transaction
        }
    }

    private val searchResultsModel = mock<SearchResultsModel> {

        on { resultsModels }.thenReturn(Observable.never())
    }

    private val searchResultsModelFactory = mock<SearchResultsModelFactory> {

        on { searchResultsModel(any()) }.thenReturn(searchResultsModel)
    }

    private val searchFragment = SearchFragment(context, searchResultsModel)

    private val searchFragmentFactory = mock<SearchFragmentFactory> {

        on { searchFragment(any(), any()) }.thenReturn(searchFragment)
    }

    fun transitionCommand(): StartupTransitionCommandImp {

        return StartupTransitionCommandImp(context, fragmentManager, searchResultsModelFactory, searchFragmentFactory)
    }

    fun initialResults(): CitySearchResults {

        return CitySearchResultsStub.stubResults()
    }

    fun transitionCommandIsInvoked(command: StartupTransitionCommandImp) {

        command.invoke(CitySearchResults.emptyResults())
    }

    fun transitionCommandIsInvokedWithInitialResults(transitionCommand: StartupTransitionCommandImp, initialResults: CitySearchResults) {

        transitionCommand.invoke(initialResults)
    }

    fun transitionTargetIsSearchView() {

        Assert.assertEquals("New root fragment is not search fragment", searchFragment, rootFragment)
    }

    fun transitionCommandIsInvokedWithAnimations() {

        verify(transaction).setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    fun searchViewIsCreatedWithInitialResults(expectedResults: CitySearchResults) {

        verify(searchFragmentFactory).searchFragment(any(), eq(searchResultsModel))
        verify(searchResultsModel).setResults(expectedResults)
    }
}