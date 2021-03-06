package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.entities.ImageSearchResults
import com.example.citysearch.services.ImageSearchService
import com.example.citysearch.models.CityDetailsModelImp
import com.example.citysearch.models.ImageCarouselModel
import com.example.citysearch.stub.CitySearchResultsStub
import com.example.citysearch.stub.ImageSearchResultsStub
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@RunWith(AndroidJUnit4::class)
class DetailsModelTests {

    lateinit var steps: DetailsModelSteps

    val Given: DetailsModelSteps get() = steps
    val When: DetailsModelSteps get() = steps
    val Then: DetailsModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = DetailsModelSteps()
    }

    @Test
    fun testTitleText() {

        val searchResult = Given.searchResult()
        val titleText = Given.titleText(searchResult)
        val detailsModel = Given.detailsModelIsCreated(searchResult = searchResult)

        val textObserver = When.observeTitle(detailsModel)

        Then.observedTextIsEqualTo(titleText)
    }

    @Test
    fun testPopulation() {

        val searchResult = Given.searchResult()
        val population = Given.population(searchResult)
        val detailsModel = Given.detailsModelIsCreated(searchResult = searchResult)

        val populationObserver = When.observePopulation(detailsModel)

        Then.observedPopulationIsEqualTo(population)
    }

    @Test
    fun testRequestImageSearch() {

        val searchResult = Given.searchResult()
        val imageSearchService = Given.imageSearchService()
        val query = Given.imageSearchQuery(searchResult)

        val detailsModel = When.detailsModelIsCreated(searchResult = searchResult, imageSearchService = imageSearchService)

        Then.imageSearchIsRequestedWithQuery(query, imageSearchService)
    }

    @Test
    fun testPassImageSearchResultsToCarouselModel() {

        val searchResult = Given.searchResult()
        val imageSearchService = Given.imageSearchService()
        val carouselModel = Given.imageCarouselModel()
        val imageResults = Given.imageResults()
        val imageURLs = Given.imageURLs(imageResults)
        val resultsQueue = Given.resultsQueue()
        val detailsModel = Given.detailsModelIsCreated(searchResult = searchResult, imageCarouselModel = carouselModel, imageSearchService = imageSearchService, resultsQueue = resultsQueue)

        When.imageServiceReturnsImageResults(imageSearchService, imageResults)

        Then.carouselModelResultsAreSetToURLsOnQueue(carouselModel, imageURLs, resultsQueue)
    }

    @Test
    fun testShowLoader() {

        val searchResult = Given.searchResult()
        val detailsModel = Given.detailsModelIsCreated(searchResult = searchResult)

        val loadingObserver = When.observeLoading(detailsModel)

        Then.observedLoadingIsEqualTo(true)
    }

    @Test
    fun testHideLoader() {

        val searchResult = Given.searchResult()
        val imageSearchService = Given.imageSearchService()
        val resultsQueue = Given.resultsQueue()
        val detailsModel = Given.detailsModelIsCreated(searchResult = searchResult, resultsQueue = resultsQueue)
        val loadingObserver = Given.observeLoading(detailsModel)
        val imageResults = Given.imageResults()

        When.imageServiceReturnsImageResults(imageSearchService, imageResults)

        Then.observedLoadingIsExpectedAndOnQueue(false, resultsQueue)
    }
}

class DetailsModelSteps {

    private var observedLoading = false

    private var observedTitle: String? = null
    private var observedPopulation: Int? = null

    private var carouselModelResults: List<URL>? = null

    private var isOnResultsQueue = false
    private var resultsSetOnResultsQueue = false

    private var observedLoadingSetOnResultsQueue = false

    private val imageSearch = BehaviorSubject.create<ImageSearchResults>()
    private val imageSearchService = mock<ImageSearchService> {

        on { imageSearch(any()) }.thenReturn(imageSearch)
    }

    private val imageCarouselResultsCaptor = argumentCaptor<Observable<List<URL>>>()

    private val imageCarouselModel = mock<ImageCarouselModel>()
    
    private val resultsQueue = TestScheduler()

    init {

        doNothing().whenever(imageCarouselModel).results = imageCarouselResultsCaptor.capture()
    }

    fun searchResult(): CitySearchResult {

        return CitySearchResultsStub.stubResults().results[0]
    }

    fun titleText(searchResult: CitySearchResult): String {

        return searchResult.nameAndState
    }

    fun population(searchResult: CitySearchResult): Int {

        return searchResult.population
    }

    fun imageSearchService(): ImageSearchService {

        return imageSearchService
    }

    fun imageSearchQuery(city: CitySearchResult): String {

        return city.nameAndState
    }

    fun imageResults(): ImageSearchResults {

        return ImageSearchResultsStub.stubResults()
    }

    fun imageCarouselModel(): ImageCarouselModel {

        return imageCarouselModel
    }

    fun imageURLs(imageResults: ImageSearchResults): List<URL> {

        return imageResults.images_results.mapNotNull { result -> result.original?.let { original -> URL(original) } }
    }

    fun imageServiceReturnsImageResults(imageSearchService: ImageSearchService, imageResults: ImageSearchResults) {

        imageSearch.onNext(imageResults)
    }

    fun resultsQueue(): Scheduler {

        return resultsQueue
    }

    fun observeLoading(model: CityDetailsModelImp): Disposable {

        return model.loading.subscribe { loading ->
            
            observedLoading = loading
            observedLoadingSetOnResultsQueue = isOnResultsQueue
        }
    }

    fun detailsModelIsCreated(searchResult: CitySearchResult, imageCarouselModel: ImageCarouselModel = this.imageCarouselModel, imageSearchService: ImageSearchService = this.imageSearchService, resultsQueue: Scheduler = this.resultsQueue): CityDetailsModelImp {

        return CityDetailsModelImp(
            searchResult,
            imageCarouselModel,
            imageSearchService,
            resultsQueue
        )
    }
    
    fun observeTitle(model: CityDetailsModelImp): Disposable {

        return model.title.subscribe { title ->
            
            observedTitle = title
        }
    }

    fun observePopulation(model: CityDetailsModelImp): Disposable {

        return model.population.subscribe { population ->
            
            observedPopulation = population
        }
    }

    fun observedTextIsEqualTo(expectedText: String) {

        Assert.assertEquals("Observed title is not correct", expectedText, observedTitle)
    }

    fun observedPopulationIsEqualTo(expectedPopulation: Int) {

        Assert.assertEquals("Observed population is not correct", expectedPopulation, observedPopulation)
    }

    fun imageSearchIsRequestedWithQuery(expectedQuery: String, searchService: ImageSearchService) {

        verify(imageSearchService, times(1)).imageSearch(expectedQuery)
    }

    fun carouselModelResultsAreSetToURLsOnQueue(carouselModel: ImageCarouselModel, expectedUrls: List<URL>, resultsQueue: Scheduler) {

        imageCarouselResultsCaptor.firstValue.subscribe { results ->

            carouselModelResults = results
            resultsSetOnResultsQueue = isOnResultsQueue
        }

        isOnResultsQueue = true
        this.resultsQueue.triggerActions()
        isOnResultsQueue = false

        Assert.assertEquals("Carousel model results were not set to correct results", expectedUrls, carouselModelResults)
        Assert.assertTrue("Carousel model results were not set on results queue", resultsSetOnResultsQueue)
    }

    fun observedLoadingIsEqualTo(expectedLoading: Boolean) {

        isOnResultsQueue = true
        this.resultsQueue.triggerActions()
        isOnResultsQueue = false

        Assert.assertEquals("Observed loading flag is not correct", expectedLoading, observedLoading)
    }

    fun observedLoadingIsExpectedAndOnQueue(expectedLoading: Boolean, resultsQueue: Scheduler) {

        observedLoadingIsEqualTo(expectedLoading)
        Assert.assertTrue("Observed loading flag was not set on results queue", observedLoadingSetOnResultsQueue)
    }
}