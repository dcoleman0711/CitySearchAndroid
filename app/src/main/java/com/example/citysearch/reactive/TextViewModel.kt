package com.example.citysearch.reactive

import android.graphics.Typeface
import com.example.citysearch.utilities.Font

// Data class for the presentation state of a TextView.  Contains the displayed text and font
data class TextViewModel(val text: String, val font: Font) {

    companion object {

        val emptyData = TextViewModel("", Font(Typeface.DEFAULT, 12.0))
    }
}