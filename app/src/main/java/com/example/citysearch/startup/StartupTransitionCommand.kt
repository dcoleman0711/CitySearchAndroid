package com.example.citysearch.startup

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.search.SearchRoot
import com.example.citysearch.search.SearchView
import com.example.citysearch.utilities.IntentFactory
import com.example.citysearch.utilities.IntentFactoryImp
import com.google.gson.Gson

interface StartupTransitionCommand {

    fun invoke(initialResults: CitySearchResults)
}

class StartupTransitionCommandImp(private val context: Context,
                                  private val intentFactory: IntentFactory): StartupTransitionCommand {

    constructor(context: Context) : this(context, IntentFactoryImp())

    override fun invoke(initialResults: CitySearchResults) {

        val intent = intentFactory.intent(context, SearchRoot::class.java)
        intent.putExtra(SearchRoot.initialResultsKey, Gson().toJson(initialResults))
        val options = ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        context.startActivity(intent, options.toBundle())
    }
}