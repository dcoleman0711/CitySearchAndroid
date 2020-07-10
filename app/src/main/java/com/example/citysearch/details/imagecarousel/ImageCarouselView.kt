package com.example.citysearch.details.imagecarousel

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citysearch.R
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageCell
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModel
import com.example.citysearch.reactive.RecyclerViewBinder
import com.example.citysearch.reactive.RecyclerViewBinderImp
import io.reactivex.disposables.Disposable

/**
 * View for ImageCarousel MVVM.
 *
 * Handles binding the RecyclerView to the view-model
 */
interface ImageCarouselView {

    val view: View
}

class ImageCarouselViewImp(override val view: RecyclerView,
                           private val viewModel: ImageCarouselViewModel,
                           private val binder: RecyclerViewBinder<AsyncImageViewModel, AsyncImageCell>): ImageCarouselView {

    private lateinit var cellsBinding: Disposable

    constructor(context: Context,
                view: RecyclerView,
                viewModel: ImageCarouselViewModel) : this(view, viewModel, RecyclerViewBinderImp<AsyncImageViewModel, AsyncImageCell>(context, R.layout.asyncimagecell, ::AsyncImageCell))

    init {

        bindView()
    }

    private fun bindView() {

        cellsBinding = binder.bindCells(view, viewModel.results)
    }
}