package com.example.citysearch.viewmodels

import android.graphics.Typeface
import androidx.lifecycle.ViewModel
import com.example.citysearch.models.StartupModel
import com.example.citysearch.utilities.Font
import io.reactivex.Observable

/**
 * ViewModel for Startup MVVM.
 *
 * Defines the presentation of the title text
 */
interface StartupViewModel {

    val model: StartupModel

    val appTitle: Observable<TextViewModel>
}

class StartupViewModelImp(override val model: StartupModel) : StartupViewModel, ViewModel() {

    private val titleFont = Font(Typeface.DEFAULT, 64.0)

    override val appTitle = model.appTitle.map { appTitle ->
        TextViewModel(
            text = appTitle,
            font = titleFont
        )
    }
}