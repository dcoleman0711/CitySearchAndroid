package com.example.citysearch.unit

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.search.SearchRoot
import com.example.citysearch.startup.StartupTransitionCommandImp
import com.example.citysearch.stub.CitySearchResultsStub
import com.example.citysearch.utilities.IntentFactory
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.*
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

        steps = StartupTransitionSteps()
    }

    @Test
    fun testTransitionTargetActivity() {

        val searchViewRootType = Given.searchRootType()
        val transitionCommand = Given.transitionCommand()

        When.transitionCommandIsInvoked(transitionCommand)

        Then.transitionTargetActivityTypeIs(searchViewRootType)
    }

    @Test
    fun testTransitionAnimations() {

        val animations = Given.animations()
        val transitionCommand = Given.transitionCommand()

        When.transitionCommandIsInvoked(transitionCommand)

        Then.transitionCommandIsInvokedWithAnimations(animations)
    }


    @Test
    fun testTransitionInitialData() {

        val initialResults = Given.initialResults()
        val transitionCommand = Given.transitionCommand()

        When.transitionCommandIsInvokedWithInitialResults(transitionCommand, initialResults)

        Then.searchViewIsCreatedWithInitialResults(initialResults)
    }
}

class StartupTransitionSteps {

    private val context = mock<Context> {  }

    private val intent = mock<Intent> {  }

    private val searchRootType = SearchRoot::class.java

    private val intentFactory = mock<IntentFactory> {

        on { intent(context, searchRootType) }.thenReturn(intent)
    }

    fun searchRootType(): Class<SearchRoot> {

        return searchRootType
    }

    fun animations(): Bundle {

        val options = ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        return options.toBundle()
    }

    fun transitionCommand(): StartupTransitionCommandImp {

        return StartupTransitionCommandImp(context, intentFactory)
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

    fun transitionTargetActivityTypeIs(expectedType: Class<SearchRoot>) {

        verify(intentFactory).intent(any(), eq(expectedType))
    }

    fun transitionCommandIsInvokedWithAnimations(animations: Bundle) {

        val intentCaptor = argumentCaptor<Intent>()
        val bundleCaptor = argumentCaptor<Bundle>()
        verify(context).startActivity(intentCaptor.capture(), bundleCaptor.capture())
        Assert.assertEquals("Transition was not invoked", intent, intentCaptor.firstValue)
        Assert.assertEquals("Transition was not invoked with correct animations", animations.toString(), bundleCaptor.firstValue.toString())
    }

    fun searchViewIsCreatedWithInitialResults(expectedResults: CitySearchResults) {

        val resultsStr = Gson().toJson(expectedResults)
        verify(intent).putExtra(eq(SearchRoot.initialResultsKey), eq(resultsStr))
    }
}