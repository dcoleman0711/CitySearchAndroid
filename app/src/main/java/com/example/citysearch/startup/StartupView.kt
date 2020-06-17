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

class StartupViewImp(context: Context,
                     private val viewModel: StartupViewModel,
                     override val view: ConstraintLayout,
                     private val appTitleLabel: RollingAnimationLabel,
                     private val constraintSetFactory: ConstraintSetFactory): StartupView {

    private lateinit var appTitleBinding: Disposable

    constructor(context: Context) : this(context, StartupViewModelImp(transitionCommand = StartupTransitionCommandImp(context)), ConstraintLayout(context), RollingAnimationLabel(context), ConstraintSetFactoryImp())

    init {

        setupView()
        buildLayout()

        viewModel.model.startTransitionTimer()

        bindViews()
    }

    private fun setupView() {

        view.id = R.id.view
        view.setBackgroundColor(Color.WHITE)
        view.clipChildren = false

        appTitleLabel.id = R.id.appTitleLabel
        view.addView(appTitleLabel)
    }

    private fun buildLayout() {

        val constraints = constraintSetFactory.constraintSet()

        // AppTitle
        constraints.centerHorizontally(appTitleLabel.id, ConstraintSet.PARENT_ID)
        constraints.centerVertically(appTitleLabel.id, ConstraintSet.PARENT_ID)
        constraints.constrainWidth(appTitleLabel.id, ConstraintSet.WRAP_CONTENT)
        constraints.constrainHeight(appTitleLabel.id, ConstraintSet.WRAP_CONTENT)

        constraints.applyTo(view)
    }

    private fun bindViews() {

        appTitleBinding = viewModel.appTitle.subscribe { viewModel ->

            appTitleLabel.start(viewModel.text, viewModel.font)
        }
    }
}