package com.example.citysearch.startup

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.R
import com.example.citysearch.animations.RollingAnimationLabel
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.utilities.TextViewUtilities

open class StartupView: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val view = ConstraintLayout(this)
        view.id = R.id.view
        setContentView(view)

        appTitleLabel = RollingAnimationLabel(this)
        appTitleLabel.id = R.id.appTitleLabel

        setupView(view)
        buildLayout(view)

        viewModel.model.startTransitionTimer()

        bindViews()
    }

    private fun setupView(view: ConstraintLayout) {

        view.setBackgroundColor(Color.WHITE)

        view.addView(appTitleLabel)
    }

    private fun buildLayout(view: ConstraintLayout) {

        val constraints = ConstraintSet()

        // AppTitle
        constraints.centerHorizontally(appTitleLabel.id, view.id)
        constraints.centerVertically(appTitleLabel.id, view.id)

        constraints.constrainWidth(appTitleLabel.id, ConstraintSet.WRAP_CONTENT)
        constraints.constrainHeight(appTitleLabel.id, ConstraintSet.WRAP_CONTENT)

        constraints.applyTo(view)
    }

    @SuppressLint("CheckResult")
    private fun bindViews() {

        viewModel.appTitle.subscribe { viewModel ->

            appTitleLabel.start(viewModel.text, viewModel.font)
        }
    }

    val view: View get() = findViewById(R.id.content)

    private lateinit var appTitleLabel: RollingAnimationLabel

    private val viewModel: StartupViewModel = StartupViewModelImp()

    private val viewBinder = ViewBinder()
}