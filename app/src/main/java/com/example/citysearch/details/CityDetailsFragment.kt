package com.example.citysearch.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.imagecarousel.ImageCarouselModelImp
import com.example.citysearch.details.imagecarousel.ImageCarouselViewImp
import com.example.citysearch.details.imagecarousel.ImageCarouselViewModelImp
import com.example.citysearch.details.map.MapModelImp
import com.example.citysearch.details.map.MapViewImp
import com.example.citysearch.details.map.MapViewModelImp

open class CityDetailsFragment(context: Context, searchResult: CitySearchResult): Fragment() {

    private val mapViewModel: MapViewModelImp
    private val imageCarouselViewModel: ImageCarouselViewModelImp

    private val viewModel: CityDetailsViewModelImp

    private lateinit var detailsView: CityDetailsView

    init {

        val mapModel = MapModelImp(searchResult)
        mapViewModel = MapViewModelImp(context, mapModel)

        val imageCarouselModel = ImageCarouselModelImp(context)
        imageCarouselViewModel = ImageCarouselViewModelImp(imageCarouselModel)

        val model = CityDetailsModelImp(searchResult, imageCarouselModel)
        viewModel = CityDetailsViewModelImp(model)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(context).inflate(R.layout.citydetails, null) as ConstraintLayout

        val context = requireContext()

        val mapView = MapViewImp(context, view.findViewById(R.id.mapView), mapViewModel)
        val imageCarouselView = ImageCarouselViewImp(context, view.findViewById(R.id.imageCarouselView), imageCarouselViewModel)

        detailsView = CityDetailsViewImp(view, mapView, imageCarouselView, viewModel)

        return view
    }
}