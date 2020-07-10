package com.example.citysearch.viewmodels

import android.content.Context
import android.graphics.Point
import android.util.Size
import androidx.lifecycle.ViewModel
import com.example.citysearch.models.SearchResultsModel
import com.example.citysearch.models.CitySearchResultModel
import com.example.citysearch.factories.CitySearchResultViewModelFactory
import com.example.citysearch.factories.CitySearchResultViewModelFactoryImp
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * ViewModel for SearchResults MVVM.
 *
 * Handles building the RecyclerViewModel out of the model's list of cell models.
 * Also provides a way to accept the recycler view's content offset event stream from the view
 */
interface SearchResultsViewModel {

    val resultsViewModels: Observable<RecyclerViewModel<CitySearchResultViewModel>>

    val contentOffset: Observable<Point>

    // In a better world, the SDK components would be following something like MVVM too,
    // and a RecyclerView would delegate state like its content offset to its view-model.
    // Then this view-model would own that as a child view-model.
    // Unfortunately, almost all the time SDK components are all bundled together as a single class,
    // so we have to delegate this from the view back to the view-model
    fun provideContentOffset(contentOffset: Observable<Point>)
}

class SearchResultsViewModelImp(
    private val context: Context,
    private val model: SearchResultsModel, // Held on this class to keep it alive
    private val viewModelFactory: CitySearchResultViewModelFactory
): SearchResultsViewModel, ViewModel() {

    override val resultsViewModels: Observable<RecyclerViewModel<CitySearchResultViewModel>>

    private val contentOffsetStreams = BehaviorSubject.create<Observable<Point>>()

    override val contentOffset = contentOffsetStreams.flatMap { point -> point }

    private val cellSize = Size(128, 128)
    private val horSpacing = 16
    private val verSpacing = 28

    constructor(context: Context, model: SearchResultsModel): this(context, model,
        CitySearchResultViewModelFactoryImp()
    )

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

        return CellData(
            viewModel,
            cellSize,
            viewModel.openDetailsCommand
        )
    }

    private fun cellViewModel(
        cellData: List<CellData<CitySearchResultViewModel>>
    ): RecyclerViewModel<CitySearchResultViewModel> {

        return RecyclerViewModel(
            cells = cellData,
            horSpacing = this.horSpacing,
            verSpacing = this.verSpacing,
            horMargins = horSpacing
        )
    }
}