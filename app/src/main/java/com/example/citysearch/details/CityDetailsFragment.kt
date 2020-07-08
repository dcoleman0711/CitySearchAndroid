package com.example.citysearch.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.imagecarousel.*
import com.example.citysearch.details.map.*

// Fragment for City Details.  Handles building the various components and managing the lifecycle
open class CityDetailsFragment(private val searchResult: CitySearchResult): Fragment() {

    private lateinit var mapViewModel: MapViewModel
    private lateinit var imageCarouselViewModel: ImageCarouselViewModel

    private lateinit var viewModel: CityDetailsViewModel

    private var detailsView: CityDetailsView? = null

    override fun onAttach(context: Context) {

        super.onAttach(context)

        val mapModel = MapModelImp(searchResult)
        val mapViewModel: MapViewModelImp by viewModels { MapViewModelFactory(context, mapModel) }

        val imageCarouselModel = ImageCarouselModelImp(context)
        val imageCarouselViewModel: ImageCarouselViewModelImp by viewModels { ImageCarouselViewModelFactory(imageCarouselModel) }

        val model = CityDetailsModelImp(searchResult, imageCarouselModel)
        val viewModel: CityDetailsViewModelImp by viewModels { CityDetailsViewModelFactory(model) }

        this.mapViewModel = mapViewModel
        this.imageCarouselViewModel = imageCarouselViewModel
        this.viewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val detailsView = this.detailsView
        if(detailsView != null)
            return detailsView.view

        val view = LayoutInflater.from(context).inflate(R.layout.citydetails, null) as ConstraintLayout

        val context = requireContext()

        val mapView = MapViewImp(context, view.findViewById(R.id.mapView), mapViewModel)
        val imageCarouselView = ImageCarouselViewImp(context, view.findViewById(R.id.imageCarouselView), imageCarouselViewModel)

        this.detailsView = CityDetailsViewImp(view, mapView, imageCarouselView, viewModel)

        return view
    }

    override fun onDestroyView() {

        super.onDestroyView()

        detailsView = null
    }
}