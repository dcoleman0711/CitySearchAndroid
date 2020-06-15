package com.example.citysearch.details.imagecarousel

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageCell
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModel
import com.example.citysearch.reactive.RecyclerViewBinder
import io.reactivex.disposables.Disposable

interface ImageCarouselView {

    val view: View
}

class ImageCarouselViewImp(context: Context, private val viewModel: ImageCarouselViewModel): ImageCarouselView {

    override val view: RecyclerView

    private val binder: RecyclerViewBinder<AsyncImageViewModel, AsyncImageCell> = RecyclerViewBinder(::AsyncImageCell)

    private lateinit var cellsBinding: Disposable

    init {

        view = RecyclerView(context)

        setupView()
        bindView()
    }

    private fun setupView() {

        view.setBackgroundColor(Color.TRANSPARENT)
        view.isHorizontalScrollBarEnabled = false

        val layoutManager = GridLayoutManager(view.context, 1, GridLayoutManager.HORIZONTAL, false)
        layoutManager.orientation = GridLayoutManager.HORIZONTAL
        view.layoutManager = layoutManager
    }

    private fun bindView() {

        cellsBinding = binder.bindCells(view, viewModel.results)
    }
}