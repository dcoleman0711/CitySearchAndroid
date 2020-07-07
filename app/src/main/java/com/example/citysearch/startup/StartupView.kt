package com.example.citysearch.startup

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.R
import com.example.citysearch.animations.RollingAnimationLabel
import com.example.citysearch.utilities.*
import io.reactivex.disposables.Disposable

interface StartupView {

    val view: View
}

class StartupViewImp(private val viewModel: StartupViewModel,
                     override val view: View,
                     private val appTitleLabel: RollingAnimationLabel): StartupView {


    private lateinit var appTitleBinding: Disposable

    constructor(view: View, viewModel: StartupViewModel) : this(viewModel, view, view.findViewById(R.id.appTitleLabel))

    init {

        viewModel.model.startTransitionTimer()

        bindViews()
    }

    private fun bindViews() {

        appTitleBinding = viewModel.appTitle.subscribe { viewModel ->

            appTitleLabel.start(viewModel.text, viewModel.font)
        }
    }
}