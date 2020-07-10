package com.example.citysearch.reactive

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import com.example.citysearch.utilities.TextViewUtilities
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*

fun<T> Optional<T>.toNullable(): T? = orElse(null)

/**
 * Handles binding various "simple" widgets to observable streams of their associated displayed data
 */
interface ViewBinder {

    fun bindTextView(view: TextView, viewModelUpdates: Observable<TextViewModel>): Disposable
    fun bindImageView(view: ImageView, viewModelUpdates: Observable<Optional<Bitmap>>): Disposable
}

class ViewBinderImp: ViewBinder {

    override fun bindTextView(view: TextView, viewModelUpdates: Observable<TextViewModel>): Disposable {

        return viewModelUpdates.subscribe { viewModel ->

            view.text = viewModel.text
            TextViewUtilities.setFont(view, viewModel.font)
        }
    }

    override fun bindImageView(view: ImageView, viewModelUpdates: Observable<Optional<Bitmap>>): Disposable {

        return viewModelUpdates.subscribe { imageOpt ->

            val image = imageOpt.toNullable()

            if(image != null)
                view.setImageBitmap(image)

            else
                view.setImageDrawable(null)
        }
    }
}