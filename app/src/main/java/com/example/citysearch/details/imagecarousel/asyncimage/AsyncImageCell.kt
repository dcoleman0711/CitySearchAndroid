package com.example.citysearch.details.imagecarousel.asyncimage

import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.R
import com.example.citysearch.reactive.RecyclerCell
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.reactive.ViewBinderImp
import com.example.citysearch.utilities.ConstraintSetFactory
import com.example.citysearch.utilities.ConstraintSetFactoryImp
import io.reactivex.disposables.Disposable

class AsyncImageCell(context: Context,
                     private val imageView: ImageView,
                     private val binder: ViewBinder,
                     private val constraintSetFactory: ConstraintSetFactory): RecyclerCell<AsyncImageViewModel>(context) {

    override var viewModel: AsyncImageViewModel? = null
        set(value) {

            field = value

            unbind()

            if(value == null)
                return

            bindViews(value)
        }

    private var imageBinding: Disposable? = null

    constructor(context: Context) : this(context, ImageView(context), ViewBinderImp(), ConstraintSetFactoryImp())

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

        val constraints = constraintSetFactory.constraintSet()

        // Image View
        constraints.connect(imageView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraints.connect(imageView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        constraints.connect(imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraints.connect(imageView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        constraints.applyTo(this)
    }

    private fun bindViews(viewModel: AsyncImageViewModel) {

        imageBinding = binder.bindImageView(imageView, viewModel.image)
    }

    private fun unbind() {

        imageBinding?.dispose()
        imageBinding = null
    }
}