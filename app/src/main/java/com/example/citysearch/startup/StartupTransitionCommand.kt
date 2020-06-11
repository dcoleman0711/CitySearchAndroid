package com.example.citysearch.startup

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.Window
import com.example.citysearch.R
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.search.SearchView
import com.google.gson.Gson

interface StartupTransitionCommand {

    fun invoke(initialResults: CitySearchResults)
}

class StartupTransitionCommandImp(private val context: Context): StartupTransitionCommand {

    override fun invoke(initialResults: CitySearchResults) {

        val intent = Intent(context, SearchView::class.java)
        intent.putExtra(SearchView.initialResultsKey, Gson().toJson(initialResults))
        val options = ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        context.startActivity(intent, options.toBundle())
    }
}