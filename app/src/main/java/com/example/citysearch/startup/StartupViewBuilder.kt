package com.example.citysearch.startup

import android.content.Context
import com.example.citysearch.animations.RollingAnimationLabel
import com.example.citysearch.data.CitySearchService
import com.example.citysearch.data.CitySearchServiceImp

class StartupViewBuilder {

    var appTitleLabel: RollingAnimationLabel? = null
    var transitionCommand: StartupTransitionCommand? = null
    var searchService: CitySearchService = CitySearchServiceImp()

    fun build(context: Context): StartupView {

        val transitionCommand = this.transitionCommand ?: StartupTransitionCommandImp(context)
        val model = StartupModelImp(transitionCommand, searchService)
        val viewModel = StartupViewModelImp(model)

        val appTitleLabel = this.appTitleLabel ?: RollingAnimationLabel(context)

        return StartupViewImp(context, viewModel, appTitleLabel)
    }
}