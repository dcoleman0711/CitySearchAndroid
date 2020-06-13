package com.example.citysearch.reactive

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout

open class RecyclerCell<ViewModel>(context: Context): ConstraintLayout(context) {

    open var viewModel: ViewModel? = null
}