package com.example.citysearch.acceptance

import android.content.Context
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.CityDetailsView
import com.example.citysearch.details.CityDetailsViewFactory
import com.example.citysearch.search.OpenDetailsCommandFactoryImp
import com.example.citysearch.search.OpenDetailsCommandImp
import com.example.citysearch.stub.CitySearchResultsStub
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OpenDetailsCommandTests {

    lateinit var steps: OpenDetailsCommandSteps

    val Given: OpenDetailsCommandSteps get() = steps
    val When: OpenDetailsCommandSteps get() = steps
    val Then: OpenDetailsCommandSteps get() = steps

    @Before
    fun setUp() {
        
        steps = OpenDetailsCommandSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testOpenDetailsCommand() {

        val searchResult = Given.searchResult()
        val searchScreen = Given.searchScreen()
        val navigationStack = Given.navigationStack(searchScreen)
        val openDetailsCommand = Given.openDetailsCommand(searchResult, searchScreen)
        val detailsScreen = Given.detailsScreen(searchResult)

        When.invoke(openDetailsCommand)

        Then.detailsScreenIsPushedOntoStack(detailsScreen, navigationStack)
    }
}

class OpenDetailsCommandSteps(private val context: Context) {

    private val cityDetailsViewFactory = mock<CityDetailsViewFactory> {  }

    private val searchView = mock<SearchView> {  }

    private val pushedFragments = ArrayList<Fragment>()

    private val fragmentManager = mock<FragmentManager> {

        on { beginTransaction() }.then { invocation ->

            val transaction = mock<FragmentTransaction> {  }

            var fragment: Fragment? = null
            var pushedFragment: Fragment? = null

            whenever(transaction.replace(any(), any())).then { invocation ->

                val viewId = invocation.getArgument<Int>(0)

                if(viewId == android.R.id.content)
                    fragment = invocation.getArgument<Fragment>(1)

                transaction
            }

            whenever(transaction.addToBackStack(anyOrNull())).then { invocation ->

                val fragment = fragment
                if(fragment != null) {
                    pushedFragment = fragment
                }

                transaction
            }

            whenever(transaction.commit()).then { invocation ->

                val fragment = pushedFragment
                if(fragment != null) {
                    pushedFragments.add(fragment)
                }

                null
            }

            transaction
        }
    }

    init {

    }

    fun searchResult(): CitySearchResult {

        return CitySearchResultsStub.stubResults().results[0]
    }

    fun searchScreen(): SearchView {

        return searchView
    }

    fun navigationStack(searchScreen: SearchView): FragmentManager {

        return fragmentManager
    }

    fun openDetailsCommand(searchResult: CitySearchResult, searchScreen: SearchView): OpenDetailsCommandImp {

        val factory = OpenDetailsCommandFactoryImp(context, fragmentManager, cityDetailsViewFactory)
        return factory.openDetailsCommand(searchResult) as OpenDetailsCommandImp
    }

    fun detailsScreen(searchResult: CitySearchResult): CityDetailsView {

        val detailsScreen = mock<CityDetailsView>()
        
        whenever(cityDetailsViewFactory.detailsView(context, searchResult)).thenReturn(detailsScreen)
        
        return detailsScreen
    }

    fun invoke(detailsCommand: OpenDetailsCommandImp) {

        detailsCommand.invoke()
    }

    fun detailsScreenIsPushedOntoStack(detailsScreen: CityDetailsView, navigationStack: FragmentManager) {

        Assert.assertEquals("Details screen was not pushed onto navigation stack", arrayListOf(detailsScreen), pushedFragments)
    }
}