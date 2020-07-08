package com.example.citysearch.search.searchresults

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.citysearch.reactive.CellData
import com.example.citysearch.reactive.RecyclerViewModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModelFactory
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModelFactoryImp
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.Size
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface SearchResultsViewModel {

    val resultsViewModels: Observable<RecyclerViewModel<CitySearchResultViewModel>>

    val contentOffset: Observable<Point>

    fun provideContentOffset(contentOffset: Observable<Point>)
}

class SearchResultsViewModelImp(private val context: Context,
                                private val model: SearchResultsModel,
                                private val viewModelFactory: CitySearchResultViewModelFactory): SearchResultsViewModel, ViewModel() {

    override val resultsViewModels: Observable<RecyclerViewModel<CitySearchResultViewModel>>

    private val contentOffsetStreams = BehaviorSubject.create<Observable<Point>>()

    override val contentOffset = contentOffsetStreams.flatMap { point -> point }

    private val cellSize = Size(width = 128, height = 128)
    private val horSpacing = 16
    private val verSpacing = 28

    constructor(context: Context, model: SearchResultsModel): this(context, model, CitySearchResultViewModelFactoryImp())

    init {

        resultsViewModels = model.resultsModels.map { resultsModels -> mapResults(resultsModels) }
    }

    override fun provideContentOffset(contentOffset: Observable<Point>) {

        contentOffsetStreams.onNext(contentOffset)
    }

    private fun mapResults(models: List<CitySearchResultModel>): RecyclerViewModel<CitySearchResultViewModel> {

        return cellViewModel(this.cellData(models))
    }

    private fun cellData(models: List<CitySearchResultModel>): List<CellData<CitySearchResultViewModel>> {

        return models.map { model -> cellDatum(model) }
    }

    private fun cellDatum(model: CitySearchResultModel): CellData<CitySearchResultViewModel> {

        val viewModel = viewModelFactory.resultViewModel(context, model)
        return CellData(viewModel, cellSize, viewModel.openDetailsCommand)
    }

    private fun cellViewModel(cellData: List<CellData<CitySearchResultViewModel>>): RecyclerViewModel<CitySearchResultViewModel> {

        return RecyclerViewModel(cells = cellData, horSpacing = this.horSpacing, verSpacing = this.verSpacing, horMargins = horSpacing)
    }
}