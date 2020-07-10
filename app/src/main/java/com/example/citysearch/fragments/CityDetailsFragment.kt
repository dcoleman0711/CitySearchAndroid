package com.example.citysearch.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.citysearch.R
import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.factories.CityDetailsViewModelFactory
import com.example.citysearch.factories.ImageCarouselViewModelFactory
import com.example.citysearch.factories.MapViewModelFactory
import com.example.citysearch.models.CityDetailsModelImp
import com.example.citysearch.models.ImageCarouselModelImp
import com.example.citysearch.models.MapModelImp
import com.example.citysearch.ui.CityDetailsView
import com.example.citysearch.ui.CityDetailsViewImp
import com.example.citysearch.ui.ImageCarouselViewImp
import com.example.citysearch.ui.MapViewImp
import com.example.citysearch.viewmodels.*

/**
 * Fragment for City Details.
 *
 * Handles building the various components and managing the lifecycle
 */
open class CityDetailsFragment(private val searchResult: CitySearchResult): Fragment() {

    private lateinit var mapViewModel: MapViewModel
    private lateinit var imageCarouselViewModel: ImageCarouselViewModel

    private lateinit var viewModel: CityDetailsViewModel

    private var detailsView: CityDetailsView? = null

    override fun onAttach(context: Context) {

        super.onAttach(context)

        val mapModel = MapModelImp(searchResult)
        val mapViewModel: MapViewModelImp by viewModels {
            MapViewModelFactory(
                context,
                mapModel
            )
        }

        val imageCarouselModel =
            ImageCarouselModelImp(context)
        val imageCarouselViewModel: ImageCarouselViewModelImp by viewModels {
            ImageCarouselViewModelFactory(
                imageCarouselModel
            )
        }

        val model = CityDetailsModelImp(
            searchResult,
            imageCarouselModel
        )
        val viewModel: CityDetailsViewModelImp by viewModels {
            CityDetailsViewModelFactory(
                model
            )
        }

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

        val view = inflater.inflate(R.layout.citydetails, container, false) as ConstraintLayout

        val context = requireContext()

        val mapView = MapViewImp(
            context,
            view.findViewById(R.id.mapView),
            mapViewModel
        )
        val imageCarouselView = ImageCarouselViewImp(
            context,
            view.findViewById(R.id.imageCarouselView),
            imageCarouselViewModel
        )

        this.detailsView = CityDetailsViewImp(
            view,
            mapView,
            imageCarouselView,
            viewModel
        )

        return view
    }

    override fun onDestroyView() {

        super.onDestroyView()

        detailsView = null
    }
}