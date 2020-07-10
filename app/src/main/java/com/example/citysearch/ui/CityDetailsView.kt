package com.example.citysearch.ui

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.citysearch.R
import com.example.citysearch.viewmodels.CityDetailsViewModel
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.reactive.ViewBinderImp
import io.reactivex.disposables.Disposable

/**
 * View for CityDetails MVVM.  Handles binding to the ViewModel.
 *
 * This is separate from the Fragment because that allows easier testing without any direct coupling to Android classes.
 */
interface CityDetailsView {

    val view: View
}

@SuppressWarnings("unused")
open class CityDetailsViewImp(
    override val view: ConstraintLayout,
    private val titleLabel: TextView,
    private val populationTitleLabel: TextView,
    private val populationLabel: TextView,
    private val mapView: MapView, // Held as a member to keep it alive
    private val imageCarouselView: ImageCarouselView, // Held as a member to keep it alive
    private val shimmeringLoader: ShimmeringLoaderView,
    private val viewModel: CityDetailsViewModel,
    private val binder: ViewBinder
): CityDetailsView {

    private lateinit var titleBinding: Disposable
    private lateinit var populationTitleBinding: Disposable
    private lateinit var populationBinding: Disposable
    private lateinit var showLoaderBinding: Disposable

    constructor(view: ConstraintLayout,
                mapView: MapView,
                imageCarouselView: ImageCarouselView,
                viewModel: CityDetailsViewModel
    ) : this(
        view,
        view.findViewById(R.id.titleLabel),
        view.findViewById(R.id.populationTitleLabel),
        view.findViewById(R.id.populationLabel),
        mapView,
        imageCarouselView,
        ShimmeringLoaderViewImp(view.findViewById(R.id.shimmeringLoader)),
        viewModel,
        ViewBinderImp()
    )

    init {

        bindViews()
    }

    private fun bindViews() {

        titleBinding = binder.bindTextView(titleLabel, viewModel.title)
        populationTitleBinding = binder.bindTextView(populationTitleLabel, viewModel.populationTitle)
        populationBinding = binder.bindTextView(populationLabel, viewModel.population)

        showLoaderBinding = viewModel.showLoader.subscribe { showLoader ->

            if(showLoader)
                shimmeringLoader.startAnimating()

            else
                shimmeringLoader.stopAnimating()
        }
    }
}