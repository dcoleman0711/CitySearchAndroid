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
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.utilities.ViewUtilities

class CityDetailsView(context: Context,
                      private val contentView: ConstraintLayout,
                      private val titleLabel: TextView,
                      private val populationTitleLabel: TextView,
                      private val populationLabel: TextView,
                      private val mapView: View,
                      private val imageCarouselView: View,
                      private val shimmeringLoader: View,
                      private val viewModel: CityDetailsViewModel,
                      private val binder: ViewBinder): Fragment() {

    constructor(context: Context, searchResult: CitySearchResult) : this(
        context,
        ConstraintLayout(context),
        TextView(context),
        TextView(context),
        TextView(context),
        View(context),
        View(context),
        View(context),
        CityDetailsViewModelImp(),
        ViewBinder()
    )

    private val view: ScrollView

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
        contentView.addView(titleLabel)

        populationTitleLabel.id = R.id.populationTitleLabel
        contentView.addView(populationTitleLabel)

        populationLabel.id = R.id.populationLabel
        contentView.addView(populationLabel)

        mapView.setBackgroundColor(Color.RED)
        mapView.id = R.id.mapView
        contentView.addView(mapView)

        imageCarouselView.setBackgroundColor(Color.GREEN)
        imageCarouselView.id = R.id.imageCarouselView
        contentView.addView(imageCarouselView)

        shimmeringLoader.id = R.id.shimmeringLoader
        contentView.addView(shimmeringLoader)
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
        constraints.connect(populationTitleLabel.id, ConstraintSet.TOP, titleLabel.id, ConstraintSet.BOTTOM, ViewUtilities.convertToPixels(context, 32))
        constraints.constrainWidth(populationTitleLabel.id, ConstraintSet.WRAP_CONTENT)
        constraints.constrainHeight(populationTitleLabel.id, ConstraintSet.WRAP_CONTENT)

        // Population Label
        constraints.connect(populationLabel.id, ConstraintSet.LEFT, populationTitleLabel.id, ConstraintSet.RIGHT, ViewUtilities.convertToPixels(context, 8))
        constraints.connect(populationLabel.id, ConstraintSet.TOP, populationTitleLabel.id, ConstraintSet.TOP)
        constraints.connect(populationLabel.id, ConstraintSet.BOTTOM, populationTitleLabel.id, ConstraintSet.BOTTOM)
        constraints.constrainWidth(populationLabel.id, ConstraintSet.WRAP_CONTENT)
        constraints.constrainHeight(populationLabel.id, ConstraintSet.WRAP_CONTENT)

        // Map View
        constraints.connect(mapView.id, ConstraintSet.RIGHT, contentView.id, ConstraintSet.RIGHT)
        constraints.connect(mapView.id, ConstraintSet.TOP, contentView.id, ConstraintSet.TOP)
        constraints.constrainPercentWidth(mapView.id, 0.5f)
        constraints.setDimensionRatio(mapView.id, "H,2:1")

        // Image Carousel View
        constraints.connect(imageCarouselView.id, ConstraintSet.LEFT, contentView.id, ConstraintSet.LEFT)
        constraints.connect(imageCarouselView.id, ConstraintSet.RIGHT, contentView.id, ConstraintSet.RIGHT)
        constraints.connect(imageCarouselView.id, ConstraintSet.TOP, mapView.id, ConstraintSet.BOTTOM)
        constraints.connect(imageCarouselView.id, ConstraintSet.BOTTOM, contentView.id, ConstraintSet.BOTTOM)
        constraints.constrainHeight(imageCarouselView.id, ViewUtilities.convertToPixels(context, 256))

        // Shimmering Loader
        constraints.connect(shimmeringLoader.id, ConstraintSet.LEFT, imageCarouselView.id, ConstraintSet.LEFT)
        constraints.connect(shimmeringLoader.id, ConstraintSet.RIGHT, imageCarouselView.id, ConstraintSet.RIGHT)
        constraints.connect(shimmeringLoader.id, ConstraintSet.TOP, imageCarouselView.id, ConstraintSet.TOP)
        constraints.connect(shimmeringLoader.id, ConstraintSet.BOTTOM, imageCarouselView.id, ConstraintSet.BOTTOM)

        constraints.applyTo(contentView)
    }

    private fun bindViews() {


    }
}