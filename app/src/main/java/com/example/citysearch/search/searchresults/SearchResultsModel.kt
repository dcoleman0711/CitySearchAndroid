package com.example.citysearch.search.searchresults

import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.search.OpenDetailsCommandFactory
import com.example.citysearch.search.OpenDetailsCommandFactoryImp
import com.example.citysearch.search.SearchRoot
import com.example.citysearch.search.SearchView
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModelFactory
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModelFactoryImp
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

interface SearchResultsModel {

    val resultsModels: Observable<Array<CitySearchResultModel>>

    fun setResults(results: CitySearchResults)
}

class SearchResultsModelImp(private val modelFactory: CitySearchResultModelFactory, private val openDetailsCommandFactory: OpenDetailsCommandFactory): SearchResultsModel {

    override val resultsModels: Observable<Array<CitySearchResultModel>> get() = resultsSubject

    private val resultsSubject = BehaviorSubject.create<Array<CitySearchResultModel>>()

    constructor(searchRoot: SearchRoot) : this(CitySearchResultModelFactoryImp(), OpenDetailsCommandFactoryImp(searchRoot))

    override fun setResults(results: CitySearchResults) {

        val resultsModels = results.results.map { searchResult -> modelFactory.resultModel(searchResult, openDetailsCommandFactory) }.toTypedArray()
        resultsSubject.onNext(resultsModels)
    }
}