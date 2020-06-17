package com.example.citysearch.parallax

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.utilities.*
import io.reactivex.disposables.Disposable

interface ParallaxView {

    val view: View
}

class ParallaxViewImp(override val view: ConstraintLayout,
                      private val viewModel: ParallaxViewModel,
                      private val viewFactory: ViewFactory,
                      private val constraintSetFactory: ConstraintSetFactory): ParallaxView {

    private lateinit var imagesBindings: Disposable
    private lateinit var offsetsBindings: Disposable

    constructor(context: Context, viewModel: ParallaxViewModel) : this(ConstraintLayout(context), viewModel, ViewFactoryImp(context), ConstraintSetFactoryImp())

    init {

        bindViews()
    }

    private fun bindViews() {

        imagesBindings = viewModel.images.subscribe { images -> buildImageViews(images) }
        offsetsBindings = viewModel.offsets.subscribe { offsets -> updateOffsets(offsets) }
    }

    private fun buildImageViews(images: Array<Bitmap>) {

        val constraints = constraintSetFactory.constraintSet()

        view.removeAllViews()
        images.forEach { image -> buildImageView(image, constraints) }

        constraints.applyTo(view)
    }

    private fun buildImageView(image: Bitmap, constraints: ConstraintSet) {

        val imageView = viewFactory.imageView()
        imageView.setImageBitmap(image)
        imageView.id = View.generateViewId()

        view.addView(imageView)

        constraints.connect(imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraints.connect(imageView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraints.connect(imageView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
        constraints.setDimensionRatio(imageView.id, "${image.width}:${image.height}")
    }

    private fun updateOffsets(offsets: Array<Point>) {

        for(index in 0 until view.childCount) {

            if(index >= offsets.size)
                break

            val child = view.getChildAt(index)
            val offset = offsets[index]

            child.translationX = offset.x.toFloat()
        }
    }
}