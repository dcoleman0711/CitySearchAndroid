package com.example.citysearch.reactive

import android.view.View

// Base type for a RecyclerView cell that uses MVVM
interface RecyclerCell<ViewModel> {

    val view: View

    var viewModel: ViewModel?
}