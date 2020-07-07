package com.example.citysearch.reactive

import android.view.View

interface RecyclerCell<ViewModel> {

    val view: View

    var viewModel: ViewModel?
}