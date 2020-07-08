package com.example.citysearch.details.imagecarousel.asyncimage

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.citysearch.R
import com.example.citysearch.reactive.RecyclerCell
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.reactive.ViewBinderImp
import io.reactivex.disposables.Disposable

// View for Async Image MVVM.  Displays a single image in its full bounds
class AsyncImageCell(override val view: ConstraintLayout,
                     private val imageView: ImageView,
                     private val binder: ViewBinder): RecyclerCell<AsyncImageViewModel> {

    override var viewModel: AsyncImageViewModel? = null
        set(value) {

            field = value

            unbind()

            if(value == null)
                return

            bindViews(value)
        }

    private var imageBinding: Disposable? = null

    constructor(view: View) : this(view as ConstraintLayout)

    constructor(view: ConstraintLayout) : this(view, view.findViewById(R.id.imageView), ViewBinderImp())

    private fun bindViews(viewModel: AsyncImageViewModel) {

        imageBinding = binder.bindImageView(imageView, viewModel.image)
    }

    private fun unbind() {

        imageBinding?.dispose()
        imageBinding = null
    }
}