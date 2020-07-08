package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.data.GeoPoint
import com.example.citysearch.search.OpenDetailsCommand
import com.example.citysearch.search.OpenDetailsCommandFactory
import com.example.citysearch.search.searchresults.citysearchresultcell.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CitySearchResultModelTests {

    lateinit var steps: CitySearchResultModelSteps

    val Given: CitySearchResultModelSteps get() = steps
    val When: CitySearchResultModelSteps get() = steps
    val Then: CitySearchResultModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = CitySearchResultModelSteps()
    }

    @Test
    fun testTitle() {

        val searchResult = Given.searchResult()

        val model = When.modelIsCreated(searchResult = searchResult)

        Then.modelTitleIs(model, searchResult.nameAndState)
    }

    @Test
    fun testPopulationClass() {

        val searchResult = Given.searchResult()
        val populationClass = Given.populationClass(searchResult)

        val model = When.modelIsCreated(searchResult = searchResult)

        Then.modelPopulationClassIs(model, populationClass)
    }

    @Test
    fun testTapCommand() {

        val searchResult = Given.searchResult()
        val tapCommandFactory = Given.tapCommandFactory()
        val tapCommand = Given.tapCommand(tapCommandFactory, searchResult)

        val model = When.modelIsCreated(searchResult = searchResult, tapCommandFactory = tapCommandFactory)

        Then.modelTapCommandIs(model, tapCommand)
    }
}

class CitySearchResultModelSteps {

    private val openDetailsCommandFactory = mock<OpenDetailsCommandFactory> {  }

    fun searchResult(): CitySearchResult {

        // This only tests one population class.  There should be one test for each class that produces a distinct output
        return CitySearchResult("Test City", 100000, GeoPoint(0.0, 0.0), adminCode = "QZ")
    }

    fun populationClass(searchResult: CitySearchResult): PopulationClass {

        return (arrayListOf(PopulationClassSmall(), PopulationClassMedium(), PopulationClassLarge(), PopulationClassVeryLarge()).first { populationClass ->

            populationClass.range.contains(searchResult.population)
        })
    }

    fun tapCommandFactory(): OpenDetailsCommandFactory {

        return openDetailsCommandFactory
    }

    fun tapCommand(factory: OpenDetailsCommandFactory, searchResult: CitySearchResult): OpenDetailsCommand {

        val command = mock<OpenDetailsCommand> { }

        whenever(factory.openDetailsCommand(searchResult)).thenReturn(command)

        return command
    }

    fun modelIsCreated(searchResult: CitySearchResult, tapCommandFactory: OpenDetailsCommandFactory = this.openDetailsCommandFactory): CitySearchResultModelImp {

        return CitySearchResultModelFactoryImp().resultModel(searchResult, tapCommandFactory) as CitySearchResultModelImp
    }

    fun modelTitleIs(model: CitySearchResultModelImp, expectedTitle: String) {

        var observedTitle: String? = null
        val subscriber = model.title.subscribe { title -> observedTitle = title }

        Assert.assertEquals("Model title is not search result name", expectedTitle, observedTitle)
    }

    fun modelPopulationClassIs(model: CitySearchResultModelImp, expectedPopulationClass: PopulationClass) {

        var observedPopClass: PopulationClass? = null
        val subscriber = model.populationClass.subscribe { popClass -> observedPopClass = popClass }

        Assert.assertEquals("Model population class is not correct", expectedPopulationClass, observedPopClass)
    }

    fun modelTapCommandIs(model: CitySearchResultModelImp, expectedTapCommand: OpenDetailsCommand) {

        Assert.assertTrue("Model tap command is not tap command created by factory", model.openDetailsCommand === expectedTapCommand)
    }
}