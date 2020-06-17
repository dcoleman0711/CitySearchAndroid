package com.example.citysearch.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.citysearch.data.CitySearchResults
import com.example.citysearch.search.searchresults.SearchResultsModelImp
import com.google.gson.Gson

class SearchRoot: FragmentActivity() {

    companion object {

        const val initialResultsKey = "initialResults"
    }

    val view: View get() = findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val initialResults = Gson().fromJson(intent.getStringExtra(initialResultsKey), CitySearchResults::class.java)
        val searchResultsModel = SearchResultsModelImp(this)
        searchResultsModel.setResults(initialResults)

        val searchView = SearchView.searchView(this, searchResultsModel)

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, searchView, "searchView")
            .commitNow()
    }
}