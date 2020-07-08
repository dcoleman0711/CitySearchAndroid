package com.example.citysearch.search

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.citysearch.parallax.ParallaxView
import com.example.citysearch.search.searchresults.*

interface SearchView {

    val view: View
}

class SearchViewImp(override val view: ConstraintLayout,
                    private val viewModel: SearchViewModel,
                    private val searchResultsView: SearchResultsView,
                    private val parallaxView: ParallaxView): SearchView {


}