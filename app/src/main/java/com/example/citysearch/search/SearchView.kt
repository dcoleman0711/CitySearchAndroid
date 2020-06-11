package com.example.citysearch.search

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.citysearch.data.CitySearchResults
import com.google.gson.Gson

class SearchView: Activity() {

    companion object {

        const val initialResultsKey = "initialResults"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val initialResults = Gson().fromJson(intent.getStringExtra(initialResultsKey), CitySearchResults::class.java)

        val view = ConstraintLayout(this)
        view.setBackgroundColor(Color.GREEN)
        setContentView(view)
    }
}