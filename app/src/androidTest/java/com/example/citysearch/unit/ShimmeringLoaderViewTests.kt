package com.example.citysearch.unit

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Choreographer
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.animations.ShimmeringLoaderView
import com.example.citysearch.animations.ShimmeringLoaderViewImp
import com.example.citysearch.utilities.ChoreographerWrapper
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import kotlin.math.sin

@RunWith(AndroidJUnit4::class)
class ShimmeringLoaderViewTests {

    lateinit var steps: ShimmeringLoaderViewSteps

    val Given: ShimmeringLoaderViewSteps get() = steps
    val When: ShimmeringLoaderViewSteps get() = steps
    val Then: ShimmeringLoaderViewSteps get() = steps

    @Before
    fun setUp() {

        steps = ShimmeringLoaderViewSteps()
    }

    @Test
    fun testStartAnimatingDisplayAlpha() {

        val shimmerView = Given.shimmerView()

        When.shimmerViewStartAnimating(shimmerView)

        Then.shimmerViewAlphaIs(shimmerView, 1.0f)
    }

    @Test
    fun testStopAnimatingDisplayLink() {

        val shimmerView = Given.shimmerView()
        Given.shimmerViewStartAnimating(shimmerView)

        When.shimmerViewStopAnimating(shimmerView)

        Then.animationWasStopped()
    }

    @Test
    fun testStopAnimatingDisplayAlpha() {

        val shimmerView = Given.shimmerView()
        Given.shimmerViewStartAnimating(shimmerView)

        When.shimmerViewStopAnimating(shimmerView)

        Then.shimmerViewAlphaIs(shimmerView, 0.0f)
    }

    @Test
    fun testTick() {

        val shimmerView = Given.shimmerView()
        Given.shimmerViewStartAnimating(shimmerView)
        val timestamps = Given.timestamps()
        val backgroundColors = Given.backgroundColors(timestamps)

        When.displayLinkTicks(timestamps, shimmerView)

        Then.shimmerViewBackgroundColorsWere(shimmerView, backgroundColors)
    }
}

class ShimmeringLoaderViewSteps {

    private val view = mock<View> {  }

    private var frameCallback: Choreographer.FrameCallback? = null

    private val choreographer = mock<ChoreographerWrapper> {

        on { postFrameCallback(any()) }.thenAnswer { invocation ->

            frameCallback = invocation.getArgument<Any>(0) as Choreographer.FrameCallback

            null
        }
    }

    private var backgroundColors: List<Int> = ArrayList()

    fun timestamps(): List<Duration> {

        return (0 until 60).map { index -> Duration.ofNanos(index.toLong() * 1e9.toLong() / 15) }
    }

    fun backgroundColors(timestamps: List<Duration>): List<Int> {

        val intervals = timestamps.map { interval -> interval - timestamps[0] }

        return intervals.map { interval ->

            val seconds = interval.toNanos().toFloat() / 1.0e9
            val factor = sin(4.0 * seconds).toFloat() * 0.5f + 0.5f
            val grayVal = 0.2f + (0.6f - 0.2f) * factor
            Color.argb(1.0f, grayVal, grayVal, grayVal)

        }.drop(1)
    }

    fun shimmerView(): ShimmeringLoaderViewImp {

        return ShimmeringLoaderViewImp(view, choreographer)
    }

    fun shimmerViewStartAnimating(shimmerView: ShimmeringLoaderViewImp) {

        shimmerView.startAnimating()
    }

    fun shimmerViewStopAnimating(shimmerView: ShimmeringLoaderViewImp) {

        shimmerView.stopAnimating()
    }

    fun displayLinkTicks(timestamps: List<Duration>, shimmerView: ShimmeringLoaderViewImp) {

        val backgroundColorCaptor = argumentCaptor<Int>()
        doNothing().whenever(view).setBackgroundColor(backgroundColorCaptor.capture())

        for(timestamp in timestamps) {

            val frameCallback = this.frameCallback
            if(frameCallback != null)
                frameCallback.doFrame(timestamp.toNanos())
        }

        this.backgroundColors = backgroundColorCaptor.allValues
    }

    fun shimmerViewAlphaIs(shimmerView: ShimmeringLoaderView, expectedAlpha: Float) {

        val argumentCaptor = argumentCaptor<Float>()
        verify(view, atLeastOnce()).alpha = argumentCaptor.capture()
        Assert.assertEquals("View alpha is not correct", expectedAlpha, argumentCaptor.lastValue)
    }

    fun animationWasStopped() {

        val frameCallback = frameCallback
        frameCallback?.doFrame(0)
        Assert.assertEquals("Choreographer animation was not stopped", this.frameCallback, frameCallback)
    }

    fun shimmerViewBackgroundColorsWere(shimmerView: ShimmeringLoaderView, expectedBackgroundColors: List<Int>) {

        Assert.assertEquals("Background color was not animated correctly", backgroundColors, expectedBackgroundColors)
    }
}