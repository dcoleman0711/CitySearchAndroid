package com.example.citysearch.utilities

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.ceil

interface MeasureConverter {

    fun convertToPixels(dp: Int): Int
}

class MeasureConverterImp(private val displayMetrics: DisplayMetrics): MeasureConverter {

    constructor(context: Context) : this(context.resources.displayMetrics)

    override fun convertToPixels(dp: Int): Int {

        return ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics)).toInt()
    }
}