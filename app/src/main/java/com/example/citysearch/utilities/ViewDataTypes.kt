package com.example.citysearch.utilities

import android.graphics.Typeface

data class Font(val typeface: Typeface, val size: Double)

data class Point(var x: Int, var y: Int) {

    companion object {

        val zero = Point(x = 0, y = 0)
    }
}

data class Size(var width: Int, var height: Int)  {

    companion object {

        val zero = Size(width = 0, height = 0)
    }
}

data class Rect(var origin: Point, var size: Size) {

    companion object {

        val zero = Rect(origin = Point.zero, size = Size.zero)
    }

    val center: Point get() = Point(x = origin.x + size.width / 2, y = origin.y + size.height / 2)

    val maxX: Int get() = origin.x + size.width
}