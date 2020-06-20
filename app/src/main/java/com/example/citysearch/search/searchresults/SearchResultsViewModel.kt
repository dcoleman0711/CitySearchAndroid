package com.example.citysearch.search.searchresults

import android.content.Context
import com.example.citysearch.reactive.CellData
import com.example.citysearch.reactive.RecyclerViewModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModel
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModelFactory
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModelFactoryImp
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.Size
import io.reactivex.Observable

interface SearchResultsViewModel {

    val resultsViewModels: Observable<RecyclerViewModel<CitySearchResultViewModel>>

    var contentOffset: Observable<Point>
}

class SearchResultsViewModelImp(private val context: Context, private val model: SearchResultsModel, private val viewModelFactory: CitySearchResultViewModelFactory): SearchResultsViewModel {

    override val resultsViewModels: Observable<RecyclerViewModel<CitySearchResultViewModel>>

    override lateinit var contentOffset: Observable<Point>

    private val cellSize = Size(width = 128, height = 128)
    private val horSpacing = 16
    private val verSpacing = 28

    constructor(context: Context, model: SearchResultsModel): this(context, model, CitySearchResultViewModelFactoryImp())

    init {

        resultsViewModels = model.resultsModels.map { resultsModels -> mapResults(resultsModels) }
    }

    private fun mapResults(models: Array<CitySearchResultModel>): RecyclerViewModel<CitySearchResultViewModel> {

        return cellViewModel(this.cellData(models))
    }

    private fun cellData(models: Array<CitySearchResultModel>): List<CellData<CitySearchResultViewModel>> {

        return models.map { model -> cellDatum(model) }
    }

    private fun cellDatum(model: CitySearchResultModel): CellData<CitySearchResultViewModel> {

        val viewModel = viewModelFactory.resultViewModel(context, model)
        return CellData(viewModel, cellSize, viewModel.openDetailsCommand)
    }

    private fun cellViewModel(cellData: List<CellData<CitySearchResultViewModel>>): RecyclerViewModel<CitySearchResultViewModel> {

        return RecyclerViewModel(cells = cellData, horSpacing = this.horSpacing, verSpacing = this.verSpacing)
    }
}