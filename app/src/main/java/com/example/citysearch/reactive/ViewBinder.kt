package com.example.citysearch.reactive

import android.annotation.SuppressLint
import android.widget.TextView
import com.example.citysearch.utilities.TextViewUtilities
import io.reactivex.Observable

class ViewBinder {

    @SuppressLint("CheckResult")
    fun bindTextView(view: TextView, viewModelUpdates: Observable<TextViewModel>) {

        viewModelUpdates.subscribe { viewModel ->

            view.text = viewModel.text
            TextViewUtilities.setFont(view, viewModel.font)
        }
    }
}