package com.example.citysearch.details.map

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Placeholder
import com.example.citysearch.R
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.reactive.ViewBinderImp
import com.example.citysearch.utilities.*
import io.reactivex.disposables.Disposable

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

    constructor(context: Context, view: ConstraintLayout, viewModel: MapViewModel): this(
        view,
        view.findViewById(R.id.backgroundImageView),
        view.findViewById(R.id.markerImageView),
        view.findViewById(R.id.guideline),
        ViewBinderImp(),
        viewModel,
        ConstraintSetFactoryImp(),
        MeasureConverterImp(context)
    )

    private lateinit var backgroundImageBinding: Disposable
    private lateinit var markerImageBinding: Disposable
    private lateinit var markerFrameBinding: Disposable

    init {

        bindViews()
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