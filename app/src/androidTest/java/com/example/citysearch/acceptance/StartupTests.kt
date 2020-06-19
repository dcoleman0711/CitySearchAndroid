package com.example.citysearch.acceptance

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.data.CitySearchService
import com.example.citysearch.search.SearchRoot
import com.example.citysearch.search.SearchView
import com.example.citysearch.startup.StartupActivity
import com.example.citysearch.startup.StartupTransitionCommand
import com.example.citysearch.startup.StartupView
import com.example.citysearch.startup.StartupViewBuilder
import com.example.citysearch.stub.CitySearchResultsStub
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.hamcrest.Matcher
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

    @Test
    fun testTransitionToSearchView() {

        val initialData = Given.initialData()

        When.transitionToSearchViewBegins(initialData)

        Then.appTransitionedToSearchView()
    }

    @Test
    fun testTransitionToSearchViewInitialData() {

        val initialData = Given.initialData()

        When.transitionToSearchViewBegins(initialData)

        Then.searchViewInitialDataIs(initialData)
    }
}

class StartupSteps {

    private val startupViewScenario = launchActivity<StartupActivity>()
    private lateinit var startupActivity: StartupActivity

    private val searchService = mock<CitySearchService> {  }

    private var transitionCommand = mock<StartupTransitionCommand> {  }

    init {

        Intents.init()

        startupViewScenario.onActivity { activity ->

            this.startupActivity = activity
            startupActivity.startupViewBuilder.transitionCommand = transitionCommand
        }
    }

    fun initialData(): CitySearchResults {

        val initialData = CitySearchResultsStub.stubResults()

        whenever(searchService.citySearch()).thenReturn(Observable.just(initialData))

        return initialData
    }

    fun transitionToSearchViewBegins(initialResults: CitySearchResults) {

        transitionCommand.invoke(initialResults)
    }

    fun appTransitionedToSearchView() {

        Intents.intended(IntentMatchers.hasComponent(SearchRoot::class.simpleName))
    }

    fun searchViewInitialDataIs(expectedInitialData: CitySearchResults) {

        Intents.intended(IntentMatchers.hasExtra(SearchRoot.initialResultsKey, Gson().toJson(expectedInitialData)))
    }
}