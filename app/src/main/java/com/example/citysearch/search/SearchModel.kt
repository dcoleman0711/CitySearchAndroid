package com.example.citysearch.search

import android.content.Context
import com.example.citysearch.parallax.ParallaxLayer
import com.example.citysearch.parallax.ParallaxModel
import com.example.citysearch.search.searchresults.SearchResultsModel
import com.example.citysearch.utilities.ImageLoader

interface SearchModel

class SearchModelImp(context: Context, private val parallaxModel: ParallaxModel, private val searchResultsModel: SearchResultsModel): SearchModel {

    init {

        val layers = arrayOf(
            ParallaxLayer(4.0f, ImageLoader.loadImage(context, "Parallax2.jpg")),
            ParallaxLayer(2.0f, ImageLoader.loadImage(context, "Parallax1.png"))
        )

        parallaxModel.setLayers(layers)
    }
}