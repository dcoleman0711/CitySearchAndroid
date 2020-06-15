package com.example.citysearch.details.imagecarousel.asyncimage

import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.R
import com.example.citysearch.reactive.RecyclerCell
import com.example.citysearch.reactive.ViewBinder
import io.reactivex.disposables.Disposable

class AsyncImageCell(context: Context,
                     private val imageView: ImageView): RecyclerCell<AsyncImageViewModel>(context) {

    override var viewModel: AsyncImageViewModel? = null
        set(value) {

            field = value

            unbind()

            if(value == null)
                return

            bindViews(value)
        }

    constructor(context: Context) : this(context, ImageView(context))

    init {

        setupView()
        buildLayout()
    }

    private fun setupView() {

        this.id = R.id.view

        imageView.id = R.id.imageView
        addView(imageView)
    }

    private fun buildLayout() {

        val constraints = ConstraintSet()

        // Image View
        constraints.connect(imageView.id, ConstraintSet.LEFT, this.id, ConstraintSet.LEFT)
        constraints.connect(imageView.id, ConstraintSet.RIGHT, this.id, ConstraintSet.RIGHT)
        constraints.connect(imageView.id, ConstraintSet.TOP, this.id, ConstraintSet.TOP)
        constraints.connect(imageView.id, ConstraintSet.BOTTOM, this.id, ConstraintSet.BOTTOM)

        constraints.applyTo(this)
    }

    private val binder: ViewBinder = ViewBinder()

    private var imageBinding: Disposable? = null

    private fun bindViews(viewModel: AsyncImageViewModel) {

        imageBinding = binder.bindImageView(imageView, viewModel.image)
    }

    private fun unbind() {

        imageBinding?.dispose()
        imageBinding = null
    }
}