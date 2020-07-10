package com.example.citysearch.search.searchresults.citysearchresultcell

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.citysearch.R
import com.example.citysearch.reactive.RecyclerCell
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.reactive.ViewBinderImp
import io.reactivex.disposables.Disposable

/**
 * View for CitySearchResult MVVM.
 *
 * Handles binding to the view-model
 */
class CitySearchResultCell(override val view: ConstraintLayout,
                           private val titleLabel: TextView,
                           private val imageView: ImageView,
                           private val binder: ViewBinder): RecyclerCell<CitySearchResultViewModel> {

    constructor(view: View) : this(view as ConstraintLayout)

    constructor(view: ConstraintLayout) : this(view, view.findViewById(R.id.titleLabel), view.findViewById(R.id.imageView), ViewBinderImp())

    override var viewModel: CitySearchResultViewModel? = null
    set(value) {

        field = value

        unbind()

        if(value == null)
            return

        bindToViewModel(value)
    }

    private var titleBinding: Disposable? = null
    private var imageBinding: Disposable? = null

    init {

        imageView.clipToOutline = true // LOL: https://issuetracker.google.com/issues/37036728
    }

    private fun bindToViewModel(viewModel: CitySearchResultViewModel) {

        titleBinding = binder.bindTextView(titleLabel, viewModel.title)
        imageBinding = binder.bindImageView(imageView, viewModel.iconImage)
    }

    private fun unbind() {

        titleBinding?.dispose()
        titleBinding = null

        imageBinding?.dispose()
        imageBinding = null
    }
}