package com.example.citysearch.details

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class CityDetailsView(context: Context): Fragment() {

    private val view: ConstraintLayout

    init {

        view = ConstraintLayout(context)
        view.setBackgroundColor(Color.MAGENTA)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return view
    }
}