package com.example.citysearch.details.map

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.constraintlayout.widget.Placeholder
import com.example.citysearch.R
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.reactive.ViewBinderImp
import com.example.citysearch.utilities.*
import io.reactivex.disposables.Disposable
import java.util.*

interface MapView {

    val view: View
}

class MapViewImp(override val view: ConstraintLayout,
                 private val backgroundImageView: ImageView,
                 private val markerImageView: ImageView,
                 private val placeholder: Placeholder,
                 private val binder: ViewBinder,
                 private val viewModel: MapViewModel,
                 private val constraintSetFactory: ConstraintSetFactory,
                 private val measureConverter: MeasureConverter): MapView {

    companion object {

        fun mapView(context: Context, viewModel: MapViewModel): MapViewImp {

            val view = ConstraintLayout(context)

            val backgroundImageView = ImageView(context)
            val markerImageView = ImageView(context)

            val placeholder = Placeholder(context)

            return MapViewImp(view, backgroundImageView, markerImageView, placeholder, ViewBinderImp(), viewModel, ConstraintSetFactoryImp(), MeasureConverterImp(context))
        }
    }

    private lateinit var backgroundImageBinding: Disposable
    private lateinit var markerImageBinding: Disposable
    private lateinit var markerFrameBinding: Disposable

    init {

        setupView()
        buildLayout()

        bindViews()
    }

    private fun setupView() {

        view.id = R.id.view

        backgroundImageView.id = R.id.backgroundImageView
        view.addView(backgroundImageView)

        markerImageView.id = R.id.markerImageView
        view.addView(markerImageView)

        placeholder.id = R.id.guideline
        view.addView(placeholder)
    }

    private fun buildLayout() {

        val constraints = constraintSetFactory.constraintSet()

        // Background Image View
        constraints.connect(backgroundImageView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraints.connect(backgroundImageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraints.connect(backgroundImageView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        constraints.connect(backgroundImageView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraints.constrainWidth(backgroundImageView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraints.constrainHeight(backgroundImageView.id, ConstraintSet.MATCH_CONSTRAINT)

        // Marker Image
        constraints.connect(placeholder.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraints.connect(placeholder.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraints.connect(markerImageView.id, ConstraintSet.LEFT, placeholder.id, ConstraintSet.RIGHT)
        constraints.connect(markerImageView.id, ConstraintSet.BOTTOM, placeholder.id, ConstraintSet.BOTTOM)

        constraints.applyTo(view)
    }

    private fun bindViews() {

        backgroundImageBinding = binder.bindImageView(backgroundImageView, viewModel.backgroundImage)
        markerImageBinding = binder.bindImageView(markerImageView, viewModel.markerImage)

        markerFrameBinding = viewModel.markerFrame.subscribe { markerFrame -> updateMarkerFrame(markerFrame) }
    }

    private fun updateMarkerFrame(markerFrame: Rect) {

        val constraints = constraintSetFactory.constraintSet()
        constraints.clone(view)

        // The marker frame size is in dp, the position is in *percent* of the total map
        constraints.constrainWidth(markerImageView.id, measureConverter.convertToPixels(markerFrame.size.width))
        constraints.constrainHeight(markerImageView.id, measureConverter.convertToPixels(markerFrame.size.height))

        constraints.constrainPercentWidth(placeholder.id, markerFrame.origin.x.toFloat() / MapViewModel.resolution)
        constraints.constrainPercentHeight(placeholder.id, markerFrame.origin.y.toFloat() / MapViewModel.resolution)

        constraints.applyTo(view)
    }
}