package com.example.citysearch.parallax

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.utilities.Point
import io.reactivex.disposables.Disposable

interface ParallaxView {

    val view: View
}

class ParallaxViewImp(private val context: Context, private val viewModel: ParallaxViewModel): ParallaxView {

    override val view: ConstraintLayout

    private lateinit var imagesBindings: Disposable
    private lateinit var offsetsBindings: Disposable

    init {

        view = ConstraintLayout(context)

        bindViews()
    }

    private fun bindViews() {

        imagesBindings = viewModel.images.subscribe { images -> buildImageViews(images) }
        offsetsBindings = viewModel.offsets.subscribe { offsets -> updateOffsets(offsets) }
    }

    private fun buildImageViews(images: Array<Bitmap>) {

        val constraints = ConstraintSet()

        view.removeAllViews()
        images.forEach { image -> buildImageView(image, constraints) }

        constraints.applyTo(view)
    }

    private fun buildImageView(image: Bitmap, constraints: ConstraintSet) {

        val imageView = ImageView(context)
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