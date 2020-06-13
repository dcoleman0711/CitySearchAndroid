package com.example.citysearch.reactive

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import com.example.citysearch.utilities.TextViewUtilities
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class ViewBinder {

    fun bindTextView(view: TextView, viewModelUpdates: Observable<TextViewModel>): Disposable {

        return viewModelUpdates.subscribe { viewModel ->

            view.text = viewModel.text
            TextViewUtilities.setFont(view, viewModel.font)
        }
    }

    fun bindImageView(view: ImageView, viewModelUpdates: Observable<Drawable>): Disposable {

        return viewModelUpdates.subscribe { image ->

            view.setImageDrawable(image)
        }
    }
}