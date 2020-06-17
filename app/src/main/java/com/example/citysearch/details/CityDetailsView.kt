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
import com.example.citysearch.utilities.MeasureConverter
import com.example.citysearch.utilities.MeasureConverterImp
import com.example.citysearch.utilities.ViewUtilities
import io.reactivex.disposables.Disposable

class CityDetailsView(context: Context,
                      private val contentView: ConstraintLayout,
                      private val titleLabel: TextView,
                      private val populationTitleLabel: TextView,
                      private val populationLabel: TextView,
                      private val mapView: MapView,
                      private val imageCarouselView: ImageCarouselView,
                      private val shimmeringLoader: ShimmeringLoaderView,
                      private val viewModel: CityDetailsViewModel,
                      private val binder: ViewBinder,
                      private val measureConverter: MeasureConverter): Fragment() {

    companion object {

        fun detailsView(context: Context, searchResult: CitySearchResult): CityDetailsView {

            val mapModel = MapModelImp(searchResult)
            val mapViewModel = MapViewModelImp(context, mapModel)
            val mapView = MapViewImp(context, mapViewModel)

            val imageCarouselModel = ImageCarouselModelImp(context)
            val imageCarouselViewModel = ImageCarouselViewModelImp(imageCarouselModel)
            val imageCarouselView = ImageCarouselViewImp(context, imageCarouselViewModel)

            val model = CityDetailsModelImp(searchResult, imageCarouselModel)
            val viewModel = CityDetailsViewModelImp(model)

            return CityDetailsView(context, viewModel, mapView, imageCarouselView)
        }
    }

    private val view: ScrollView

    private lateinit var titleBinding: Disposable
    private lateinit var populationTitleBinding: Disposable
    private lateinit var populationBinding: Disposable
    private lateinit var showLoaderBinding: Disposable

    constructor(context: Context,
                viewModel: CityDetailsViewModel,
                mapView: MapView,
                imageCarouselView: ImageCarouselView
    ) : this(
        context,
        ConstraintLayout(context),
        TextView(context),
        TextView(context),
        TextView(context),
        mapView,
        imageCarouselView,
        ShimmeringLoaderViewImp(context),
        viewModel,
        ViewBinderImp(),
        MeasureConverterImp(context)
    )

    init {

        view = ScrollView(context)

        bindViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupView()
        buildLayout()

        return view
    }

    private fun setupView() {

        view.id = R.id.view
        view.setBackgroundColor(Color.rgb(0.8f, 0.8f, 0.8f))
        view.isFillViewport = true

        contentView.id = R.id.contentView
        view.addView(contentView)
        
        titleLabel.id = R.id.titleLabel
        titleLabel.setTextColor(Color.BLACK)
        contentView.addView(titleLabel)

        populationTitleLabel.id = R.id.populationTitleLabel
        populationTitleLabel.setTextColor(Color.BLACK)
        contentView.addView(populationTitleLabel)

        populationLabel.id = R.id.populationLabel
        populationLabel.setTextColor(Color.BLACK)
        contentView.addView(populationLabel)

        mapView.view.id = R.id.mapView
        contentView.addView(mapView.view)

        imageCarouselView.view.id = R.id.imageCarouselView
        contentView.addView(imageCarouselView.view)

        shimmeringLoader.view.id = R.id.shimmeringLoader
        contentView.addView(shimmeringLoader.view)
    }

    private fun buildLayout() {

        val constraints = ConstraintSet()
        val context = requireContext()

        // Title Label
        constraints.connect(titleLabel.id, ConstraintSet.LEFT, contentView.id, ConstraintSet.LEFT)
        constraints.connect(titleLabel.id, ConstraintSet.TOP, contentView.id, ConstraintSet.TOP)
        constraints.constrainWidth(titleLabel.id, ConstraintSet.WRAP_CONTENT)
        constraints.constrainHeight(titleLabel.id, ConstraintSet.WRAP_CONTENT)

        // Population Title Label
        constraints.connect(populationTitleLabel.id, ConstraintSet.LEFT, titleLabel.id, ConstraintSet.LEFT)
        constraints.connect(populationTitleLabel.id, ConstraintSet.TOP, titleLabel.id, ConstraintSet.BOTTOM, measureConverter.convertToPixels(32))
        constraints.constrainWidth(populationTitleLabel.id, ConstraintSet.WRAP_CONTENT)
        constraints.constrainHeight(populationTitleLabel.id, ConstraintSet.WRAP_CONTENT)

        // Population Label
        constraints.connect(populationLabel.id, ConstraintSet.LEFT, populationTitleLabel.id, ConstraintSet.RIGHT, measureConverter.convertToPixels(8))
        constraints.connect(populationLabel.id, ConstraintSet.TOP, populationTitleLabel.id, ConstraintSet.TOP)
        constraints.connect(populationLabel.id, ConstraintSet.BOTTOM, populationTitleLabel.id, ConstraintSet.BOTTOM)
        constraints.constrainWidth(populationLabel.id, ConstraintSet.WRAP_CONTENT)
        constraints.constrainHeight(populationLabel.id, ConstraintSet.WRAP_CONTENT)

        // Map View
        constraints.connect(mapView.view.id, ConstraintSet.RIGHT, contentView.id, ConstraintSet.RIGHT)
        constraints.connect(mapView.view.id, ConstraintSet.TOP, contentView.id, ConstraintSet.TOP)
        constraints.constrainPercentWidth(mapView.view.id, 0.5f)
        constraints.setDimensionRatio(mapView.view.id, "H,2:1")

        // Image Carousel View
        constraints.connect(imageCarouselView.view.id, ConstraintSet.LEFT, contentView.id, ConstraintSet.LEFT)
        constraints.connect(imageCarouselView.view.id, ConstraintSet.RIGHT, contentView.id, ConstraintSet.RIGHT)
        constraints.connect(imageCarouselView.view.id, ConstraintSet.TOP, mapView.view.id, ConstraintSet.BOTTOM)
        constraints.connect(imageCarouselView.view.id, ConstraintSet.BOTTOM, contentView.id, ConstraintSet.BOTTOM)
        constraints.constrainHeight(imageCarouselView.view.id, measureConverter.convertToPixels(256))

        // Shimmering Loader
        constraints.connect(shimmeringLoader.view.id, ConstraintSet.LEFT, imageCarouselView.view.id, ConstraintSet.LEFT)
        constraints.connect(shimmeringLoader.view.id, ConstraintSet.RIGHT, imageCarouselView.view.id, ConstraintSet.RIGHT)
        constraints.connect(shimmeringLoader.view.id, ConstraintSet.TOP, imageCarouselView.view.id, ConstraintSet.TOP)
        constraints.connect(shimmeringLoader.view.id, ConstraintSet.BOTTOM, imageCarouselView.view.id, ConstraintSet.BOTTOM)

        constraints.applyTo(contentView)
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