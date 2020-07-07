package com.example.citysearch.details

import android.content.Context
import android.view.LayoutInflater
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.details.imagecarousel.ImageCarouselModelImp
import com.example.citysearch.details.imagecarousel.ImageCarouselViewImp
import com.example.citysearch.details.imagecarousel.ImageCarouselViewModelImp
import com.example.citysearch.details.map.MapModelImp
import com.example.citysearch.details.map.MapViewImp
import com.example.citysearch.details.map.MapViewModelImp

interface CityDetailsViewFactory {

    fun detailsView(context: Context, searchResult: CitySearchResult): CityDetailsView
}

class CityDetailsViewFactoryImp: CityDetailsViewFactory {

    override fun detailsView(context: Context, searchResult: CitySearchResult): CityDetailsView {

        val view = LayoutInflater.from(context).inflate(R.layout.citydetails, null) as ScrollView

        val mapModel = MapModelImp(searchResult)
        val mapViewModel = MapViewModelImp(context, mapModel)
        val mapView = MapViewImp(context, view.findViewById(R.id.mapView), mapViewModel)

        val imageCarouselModel = ImageCarouselModelImp(context)
        val imageCarouselViewModel = ImageCarouselViewModelImp(imageCarouselModel)
        val imageCarouselView = ImageCarouselViewImp(context, view.findViewById(R.id.imageCarouselView), imageCarouselViewModel)

        val model = CityDetailsModelImp(searchResult, imageCarouselModel)
        val viewModel = CityDetailsViewModelImp(model)

        return CityDetailsView(view, mapView, imageCarouselView, viewModel)
    }
}