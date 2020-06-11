package com.example.citysearch.startup

import android.graphics.Typeface
import com.example.citysearch.reactive.TextViewModel
import com.example.citysearch.utilities.Font
import io.reactivex.Observable

interface StartupViewModel {

    val model: StartupModel

    val appTitle: Observable<TextViewModel>
}

class StartupViewModelImp(override val model: StartupModel) : StartupViewModel {

    private val titleFont = Font(Typeface.DEFAULT, 64.0)

    constructor(transitionCommand: StartupTransitionCommand): this(StartupModelImp(transitionCommand))

    override val appTitle = model.appTitle.map { appTitle -> TextViewModel(text = appTitle, font = titleFont) }
}