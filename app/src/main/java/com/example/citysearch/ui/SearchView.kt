package com.example.citysearch.ui

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.citysearch.viewmodels.SearchViewModel

/**
 * View for Search MVVM.
 *
 * Handles binding to the ViewModel.
 * This is separate from the Fragment because that allows easier testing without any direct coupling to Android classes.
 */
interface SearchView {

    val view: View
}

class SearchViewImp(override val view: ConstraintLayout,
                    private val viewModel: SearchViewModel,
                    private val searchResultsView: SearchResultsView,
                    private val parallaxView: ParallaxView
):
    SearchView {


}