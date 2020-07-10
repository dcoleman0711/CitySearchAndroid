package com.example.citysearch.startup

import android.view.View
import com.example.citysearch.R
import com.example.citysearch.animations.RollingAnimationLabel
import io.reactivex.disposables.Disposable

/**
 * View for Startup MVVM.
 *
 * Handles binding to the ViewModel.
 * This is separate from the Fragment because that allows easier testing without any direct coupling to Android classes.
 */
interface StartupView {

    val view: View
}

class StartupViewImp(override val view: View,
                     private val viewModel: StartupViewModel,
                     private val appTitleLabel: RollingAnimationLabel): StartupView {


    private lateinit var appTitleBinding: Disposable

    constructor(view: View, viewModel: StartupViewModel) : this(view, viewModel, view.findViewById(R.id.appTitleLabel))

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