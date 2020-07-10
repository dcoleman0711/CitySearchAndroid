package com.example.citysearch.viewmodels

import com.example.citysearch.utilities.Font

/**
 * Data class for the presentation state of a TextView.  Contains the displayed text and font
 *
 * @property text The text to display
 * @property font The font to use for the displayed text
 */
data class TextViewModel(val text: String, val font: Font)