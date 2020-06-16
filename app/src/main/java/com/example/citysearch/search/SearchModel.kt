package com.example.citysearch.search

import android.content.Context
import android.os.AsyncTask
import com.example.citysearch.parallax.ParallaxLayer
import com.example.citysearch.parallax.ParallaxModel
import com.example.citysearch.search.searchresults.SearchResultsModel
import com.example.citysearch.utilities.ImageLoader
import java.lang.ref.WeakReference
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

interface SearchModel

class SearchModelImp(private val context: Context, private val parallaxModel: ParallaxModel, private val searchResultsModel: SearchResultsModel): SearchModel {

    init {

        LoadLayersTask(WeakReference(this)).execute()
    }

    class LoadLayersTask(private val searchModel: WeakReference<SearchModelImp>): AsyncTask<Void, Void, Unit>() {

        override fun doInBackground(vararg params: Void?) {

            val searchModel = this.searchModel.get()
            if(searchModel == null)
                return

            val layers = arrayOf(
                ParallaxLayer(4.0f, ImageLoader.loadImage(searchModel.context, "Parallax2.jpg")),
                ParallaxLayer(2.0f, ImageLoader.loadImage(searchModel.context, "Parallax1.png"))
            )

            searchModel.parallaxModel.setLayers(layers)
        }
    }
}