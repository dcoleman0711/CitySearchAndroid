package com.example.citysearch.details

import android.content.Context
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

        val mapModel = MapModelImp(searchResult)
        val mapViewModel = MapViewModelImp(context, mapModel)
        val mapView = MapViewImp.mapView(context, mapViewModel)

        val imageCarouselModel = ImageCarouselModelImp(context)
        val imageCarouselViewModel = ImageCarouselViewModelImp(imageCarouselModel)
        val imageCarouselView = ImageCarouselViewImp(context, imageCarouselViewModel)

        val model = CityDetailsModelImp(searchResult, imageCarouselModel)
        val viewModel = CityDetailsViewModelImp(model)

        return CityDetailsView(context, viewModel, mapView, imageCarouselView)
    }
}