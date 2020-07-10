package com.example.citysearch.viewmodels

import android.graphics.Bitmap
import android.util.Size
import androidx.lifecycle.ViewModel
import com.example.citysearch.models.ImageCarouselModel
import com.example.citysearch.models.AsyncImageModel
import com.example.citysearch.factories.AsyncImageViewModelFactory
import com.example.citysearch.factories.AsyncImageViewModelFactoryImp
import com.example.citysearch.reactive.toNullable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * ViewModel for ImageCarousel MVVM.
 *
 * Handles conversion of the the model's list of cell models into a RecyclerViewModel.
 *
 * Since the loading of one cell's image alter's the size of the associated cell, t
 * he view-model needs to ensure that an updated RecyclerViewModel is published
 * every time either the cells themselves change, or any of their images loads
 */
interface ImageCarouselViewModel {

    val results: Observable<RecyclerViewModel<AsyncImageViewModel>>
}

class ImageCarouselViewModelImp(
    private val model: ImageCarouselModel,
    private val viewModelFactory: AsyncImageViewModelFactory,
    private val workQueue: Scheduler,
    private val resultsQueue: Scheduler
): ImageCarouselViewModel, ViewModel() {

    constructor(model: ImageCarouselModel) : this(model,
        AsyncImageViewModelFactoryImp(), Schedulers.computation(), AndroidSchedulers.mainThread())

    override val results: Observable<RecyclerViewModel<AsyncImageViewModel>>

    private val itemSpacing = 32

    init {

        // The recycler view should update when the results list is published,
        // and each time any of the images loads,
        // in order to resize the cell for the loaded image.
        // A cell should be square until its image is loaded,
        // and then it should resize to have the same aspect ratio as its image.
        // To do this, we take each cell model's image observable,
        // map it to an *optional* image observable and merge it with an immediately published "null".
        // We want to publish a new view model to the recycler view on every image load,
        // so we use combineLatest, which publishes an event every time *any* of the
        // image observables publishes a new event.
        // combineLatest will only publish an event after every input stream has published
        // at least one event.
        // But since every image stream is prepended with an immediately published "null",
        // combineLatest will publish an event combining all these "null"s immediately.
        // This event is the initial recycler view load, where it knows how many cells to display,
        // but no images are loaded yet,
        // and all displayed cells will be "loading" cells
        results = model.resultsModels.flatMap { resultModels: List<AsyncImageModel> ->

            val cellDataUpdates = resultModels.map { resultModel ->

                Observable
                    .merge(
                        Observable.just(Optional.empty()),
                        resultModel.image
                            .map { image -> Optional.of(image) }
                    )
                    .subscribeOn(workQueue)
                    .map { image ->

                        cellData(resultModel, image.toNullable())
                    }
            }

            Observable.combineLatest(cellDataUpdates) { cellData ->

                RecyclerViewModel(
                    cellData.filterIsInstance<CellData<AsyncImageViewModel>>(),
                    itemSpacing,
                    0
                )

            }

        }.observeOn(resultsQueue)
    }

    private fun cellData(
        resultModel: AsyncImageModel,
        image: Bitmap?
    ): CellData<AsyncImageViewModel> {

        return CellData(
            viewModelFactory.viewModel(
                resultModel
            ), cellSize(image), null
        )
    }

    private fun cellSize(image: Bitmap?): Size {

        val cellHeight = 256
        val aspectRatio = if(image != null) image.width.toFloat() / image.height.toFloat() else 1.0f

        return Size((cellHeight.toFloat() * aspectRatio).toInt(), cellHeight)
    }
}