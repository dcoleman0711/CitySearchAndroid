package com.example.citysearch.details.imagecarousel

import android.graphics.Bitmap
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageModel
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModel
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModelFactory
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModelFactoryImp
import com.example.citysearch.reactive.CellData
import com.example.citysearch.reactive.RecyclerViewModel
import com.example.citysearch.reactive.toNullable
import com.example.citysearch.utilities.Size
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

interface ImageCarouselViewModel {

    val results: Observable<RecyclerViewModel<AsyncImageViewModel>>
}

class ImageCarouselViewModelImp(private val model: ImageCarouselModel, private val viewModelFactory: AsyncImageViewModelFactory): ImageCarouselViewModel {

    constructor(model: ImageCarouselModel) : this(model, AsyncImageViewModelFactoryImp())

    override val results: Observable<RecyclerViewModel<AsyncImageViewModel>>

    private val itemSpacing = 32

    init {

        // The recycler view should update when the results list is published, and each time any of the images loads, in order to resize the cell for the loaded image.
        // A cell should be square until its image is loaded, and then it should resize to have the same aspect ratio as its image.
        // To do this, we take each cell model's image observable, map it to an *optional* image observable and merge it with an immediately published "null".
        // We want to publish a new view model to the recycler view on every image load, so we use combineLatest, which publishes an event every time *any* of the image observables publishes a new event.
        // combineLatest will only publish an event after every input stream has published at least one event.  But since every image stream is prepended with an immediately published "null",
        // combineLatest will publish an event combining all these "null"s immediately.  This event is the initial recycler view load, where it knows how many cells to display, but no images are loaded yet,
        // and all displayed cells will be "loading" cells
        results = model.resultsModels.flatMap { resultModels: Array<AsyncImageModel> ->

            val cellDataUpdates = resultModels.map { resultModel ->

                val imageEvents = Observable.merge(Observable.just(Optional.empty()), resultModel.image.map { image -> Optional.of(image) } )
                    .subscribeOn(Schedulers.computation())

                imageEvents.map { image ->

                    cellData(resultModel, image.toNullable())

                }
            }

            Observable.combineLatest(cellDataUpdates) { cellData ->

                RecyclerViewModel(cellData.filterIsInstance<CellData<AsyncImageViewModel>>().toTypedArray(), itemSpacing, 0)

            }

        }.observeOn(AndroidSchedulers.mainThread())
    }

    private fun cellData(resultModel: AsyncImageModel, image: Bitmap?): CellData<AsyncImageViewModel> {

        return CellData(viewModelFactory.viewModel(resultModel), cellSize(image), null)
    }

    private fun cellSize(image: Bitmap?): Size {

        val cellHeight = 256
        val aspectRatio = if(image != null) image.width.toFloat() / image.height.toFloat() else 1.0f

        return Size((cellHeight.toFloat() * aspectRatio).toInt(), cellHeight)
    }
}