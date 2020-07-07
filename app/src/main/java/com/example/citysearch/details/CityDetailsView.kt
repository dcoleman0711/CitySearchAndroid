package com.example.citysearch.details

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.citysearch.R
import com.example.citysearch.animations.ShimmeringLoaderView
import com.example.citysearch.animations.ShimmeringLoaderViewImp
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.imagecarousel.ImageCarouselModelImp
import com.example.citysearch.details.imagecarousel.ImageCarouselView
import com.example.citysearch.details.imagecarousel.ImageCarouselViewImp
import com.example.citysearch.details.imagecarousel.ImageCarouselViewModelImp
import com.example.citysearch.details.map.MapModelImp
import com.example.citysearch.details.map.MapView
import com.example.citysearch.details.map.MapViewImp
import com.example.citysearch.details.map.MapViewModelImp
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.reactive.ViewBinderImp
import com.example.citysearch.utilities.*
import io.reactivex.disposables.Disposable

open class CityDetailsView(private val view: ScrollView,
                           private val contentView: ConstraintLayout,
                           private val titleLabel: TextView,
                           private val populationTitleLabel: TextView,
                           private val populationLabel: TextView,
                           private val mapView: MapView,
                           private val imageCarouselView: ImageCarouselView,
                           private val shimmeringLoader: ShimmeringLoaderView,
                           private val viewModel: CityDetailsViewModel,
                           private val binder: ViewBinder): Fragment() {

    private lateinit var titleBinding: Disposable
    private lateinit var populationTitleBinding: Disposable
    private lateinit var populationBinding: Disposable
    private lateinit var showLoaderBinding: Disposable

    constructor(view: ScrollView,
                mapView: MapView,
                imageCarouselView: ImageCarouselView,
                viewModel: CityDetailsViewModel
    ) : this(
        view,
        view.findViewById(R.id.contentView),
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return view
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