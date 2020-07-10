package com.example.citysearch.search.searchresults

import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.search.OpenDetailsCommandFactory
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModelFactory
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModelFactoryImp
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Model for SearchResults MVVM.
 *
 * Exposes a settable city search results, and maps it into a stream of lists of search result cell models
 */
interface SearchResultsModel {

    val resultsModels: Observable<List<CitySearchResultModel>>

    fun setResults(results: CitySearchResults)
}

class SearchResultsModelImp(private val modelFactory: CitySearchResultModelFactory, private val openDetailsCommandFactory: OpenDetailsCommandFactory): SearchResultsModel {

    override val resultsModels: BehaviorSubject<List<CitySearchResultModel>> = BehaviorSubject.create()

    constructor(openDetailsCommandFactory: OpenDetailsCommandFactory) : this(CitySearchResultModelFactoryImp(), openDetailsCommandFactory)

    override fun setResults(results: CitySearchResults) {

        val resultsModels = results.results.map { searchResult -> modelFactory.resultModel(searchResult, openDetailsCommandFactory) }
        this.resultsModels.onNext(resultsModels)
    }
}