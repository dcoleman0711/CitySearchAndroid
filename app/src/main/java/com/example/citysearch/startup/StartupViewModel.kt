package com.example.citysearch.startup

import android.graphics.Typeface
import androidx.lifecycle.ViewModel
import com.example.citysearch.reactive.TextViewModel
import com.example.citysearch.utilities.Font
import io.reactivex.Observable

interface StartupViewModel {

    val model: StartupModel

    val appTitle: Observable<TextViewModel>
}

class StartupViewModelImp(override val model: StartupModel) : StartupViewModel, ViewModel() {

    private val titleFont = Font(Typeface.DEFAULT, 64.0)

    override val appTitle = model.appTitle.map { appTitle -> TextViewModel(text = appTitle, font = titleFont) }
}