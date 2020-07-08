package com.example.citysearch.search

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.citysearch.parallax.ParallaxView
import com.example.citysearch.search.searchresults.*

// View for Search MVVM.  Handles binding to the ViewModel.  This is separate from the Fragment because that allows easier testing without any direct coupling to Android classes.
interface SearchView {

    val view: View
}

class SearchViewImp(override val view: ConstraintLayout,
                    private val viewModel: SearchViewModel,
                    private val searchResultsView: SearchResultsView,
                    private val parallaxView: ParallaxView): SearchView {


}