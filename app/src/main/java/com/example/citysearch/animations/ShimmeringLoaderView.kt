package com.example.citysearch.animations

import android.graphics.Color
import android.view.Choreographer
import android.view.View
import com.example.citysearch.utilities.ChoreographerWrapper
import com.example.citysearch.utilities.ChoreographerWrapperImp
import kotlin.math.sin

// Implements the loading effect seen on the image carousel
interface ShimmeringLoaderView {

    val view: View

    fun startAnimating()
    fun stopAnimating()
}

class ShimmeringLoaderViewImp(override val view: View,
                              private val choreographer: ChoreographerWrapper): ShimmeringLoaderView, Choreographer.FrameCallback {

    constructor(view: View) : this(view, ChoreographerWrapperImp())

    private var running = false

    private var startTime: Long? = null

    companion object {

        private const val darkest = 0.2f
        private const val lightest = 0.6f
    }

    init {

        view.alpha = 0.0f
    }

    override fun startAnimating() {

        view.alpha = 1.0f

        running = true
        choreographer.postFrameCallback(this)
    }

    override fun stopAnimating() {

        running = false
        view.alpha = 0.0f
    }

    override fun doFrame(frameTimeNanos: Long) {

        if(!running)
            return;

        choreographer.postFrameCallback(this)

        val startTime = this@ShimmeringLoaderViewImp.startTime
        if(startTime == null) {
            this@ShimmeringLoaderViewImp.startTime = frameTimeNanos
            return
        }

        val interval = (frameTimeNanos - startTime) / 1.0e9f

        val factor = sin(4.0 * interval).toFloat() * 0.5f + 0.5f

        val grayVal = ShimmeringLoaderViewImp.darkest + (ShimmeringLoaderViewImp.lightest - ShimmeringLoaderViewImp.darkest) * factor

        view.setBackgroundColor(Color.argb(1.0f, grayVal, grayVal, grayVal))
    }
}