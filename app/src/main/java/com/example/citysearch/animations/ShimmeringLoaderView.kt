package com.example.citysearch.animations

import android.content.Context
import android.graphics.Color
import android.view.Choreographer
import android.view.View
import kotlin.math.sin

class ShimmeringLoaderView(context: Context): View(context), Choreographer.FrameCallback {

    private val choreographer: Choreographer
    private var running = false

    private var startTime: Long? = null

    companion object {

        private const val darkest = 0.2f
        private const val lightest = 0.6f
    }

    init {

        choreographer = Choreographer.getInstance()

        stopAnimating()
    }

    fun startAnimating() {

        alpha = 1.0f

        running = true
        choreographer.postFrameCallback(this)
    }

    fun stopAnimating() {

        running = false
        alpha = 0.0f
    }

    override fun doFrame(frameTimeNanos: Long) {

        if(!running)
            return;

        choreographer.postFrameCallback(this)

        val startTime = this@ShimmeringLoaderView.startTime
        if(startTime == null) {
            this@ShimmeringLoaderView.startTime = frameTimeNanos
            return
        }

        val interval = (frameTimeNanos - startTime) / 1.0e9f

        val factor = sin(4.0 * interval).toFloat() * 0.5f + 0.5f

        val grayVal = ShimmeringLoaderView.darkest + (ShimmeringLoaderView.lightest - ShimmeringLoaderView.darkest) * factor

        setBackgroundColor(Color.argb(1.0f, grayVal, grayVal, grayVal))
    }
}