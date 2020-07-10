package com.example.citysearch.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Size
import android.view.Choreographer
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.example.citysearch.utilities.*
import java.time.Duration

/**
 * Implementation for the animation seen on the title screen
 */
open class RollingAnimationLabel(
    context: Context,
    attributeSet: AttributeSet?
): FrameLayout(context, attributeSet), Choreographer.FrameCallback {

    private var text: String = ""
    private var font: Font = Font(Typeface.DEFAULT, 12.0)

    // These can easily be exposed for configurability
    private val animationCurve: (Double) -> Double =
        easeOutBackCurve
    private val totalDuration: Duration = Duration.ofSeconds(4)
    private val characterDuration: Duration = Duration.ofSeconds(1)

    private var characterViews: Array<TextView> = arrayOf()

    private lateinit var choreographer: Choreographer

    private var startTime: Long? = null

    private var startTimes: Map<TextView, Duration> = mapOf()

    private var containerSize: Size = Size(0, 0)
    private var xFinalOffsets: Map<TextView, Int> = mapOf()

    private var running = false

    open fun start(text: String, font: Font) {

        this.text = text
        this.font = font

        running = true

        this.choreographer = Choreographer.getInstance()

        construct()

        choreographer.postFrameCallback(this)
    }

    private fun construct() {

        // Break the label up into one label for each character
        val characters = text.toCharArray()

        this.characterViews = characters.map({ char -> characterView(char) }).toTypedArray()

        // Use sizeThatFits to get the size of each character label, then accumulate the sizes to build frames left-to-right
        val sizes = characterViews.map { characterView -> ViewUtilities.intrinsicSize(characterView) }

        val frames = sizes.map { size -> Rect(0, 0, size.width, size.height) }.stream()
            .collect({ ArrayList<Rect>() }, { sums, next ->

            var x = 0
            if(!sums.isEmpty()) {
                x = sums.last().right
            }

            sums.add(Rect(x, 0, x + next.width(), next.height()))

        }, { first, second -> first.addAll(second) })

        // Now get the center of each character
        val centers = frames.map { frame -> Point(frame.centerX(), frame.centerY()) }
        val viewsAndCenters = CollectionUtilities.zip(characterViews, centers)

        // The width of the entire label is the right edge of the last frame
        val width = frames.lastOrNull()?.right ?: 0
        val height = frames.map { frame -> frame.height() }.max() ?: 0

        this.containerSize = Size(width, height)

        val containerCenter = Point(width / 2, height / 2)

        // Build the final ("destination") x offsets from the center of each label's calculated frame to the center of the container.
        // The center will be the "anchor" during the animation
        val xOffsets = viewsAndCenters.map { viewAndCenter ->

            Pair(viewAndCenter.first, viewAndCenter.second.x - containerCenter.x)
        }

        this.xFinalOffsets = xOffsets.toMap()

        // No need to animate whitespace character, no one would know the difference!
        val nonwhiteSpaceCharacterViews = characterViews.filter { label -> !label.text.first().isWhitespace() }

        // The last character's animation should end at the end of the total duration.
        // Therefore the last character should begin its animation at this duration minus the individual character duration.
        // The start times are then evenly spread between 0 and this maximum interval
        val lastAnimationStart = totalDuration - characterDuration
        val lastIndex = nonwhiteSpaceCharacterViews.size - 1
        val startTimes = (0..lastIndex).map { index -> lastAnimationStart.multipliedBy(index.toLong()).dividedBy(lastIndex.toLong()) }

        this.startTimes = CollectionUtilities.zip(nonwhiteSpaceCharacterViews, startTimes).toMap()
    }

    private fun characterView(character: Char): TextView {

        val characterView = TextView(this.context)
        characterView.setTextColor(Color.BLACK)
        characterView.text = character.toString()
        TextViewUtilities.setFont(characterView, font)

        this.addView(characterView)

        return characterView
    }

    override fun doFrame(frameTimeNanos: Long) {

        if(!running)
            return

        choreographer.postFrameCallback(this)

        // Choreographer returns uptime, and we need a point of reference, so we have to let one frame go by to define the start frame
        val startTime = this.startTime
        if(startTime == null) {
            this.startTime = frameTimeNanos
            return
        }

        val offset = Duration.ofNanos(frameTimeNanos - startTime)

        if(offset > totalDuration) {

            // The animation is compvaled.  Remove the display link so it doesn't keep this object alive
            running = false
            return
        }

        // Map each offset within the duration to the offset relative to the current time.
        // Then scale by the character duration, and we get an array of progresses (from 0 to 1) of each character animation
        val characterOffsets = this.startTimes.mapValues { viewAndStartTime -> offset - viewAndStartTime.value }
        val characterProgresses = characterOffsets.mapValues { viewAndOffset ->
            (viewAndOffset.value.toNanos().toDouble() / this.characterDuration.toNanos().toDouble()).coerceIn(0.0, 1.0)
        }

        for(viewAndProgress in characterProgresses) {

            val view = viewAndProgress.key
            val progress = viewAndProgress.value

            // The animation consists of three parts: a linear alpha fade-in, a quadratic font scaling, and a reverse linear approach into the final position.
            val alpha = progress.toFloat()
            val fontSize = this.font.size * animationCurve(progress)

            // The x constants will be scaled, starting at twice their final value.
            // The reason we picked the center of the container as the anchor is so that the characters on the left come in from the left,
            // and the characters on the right come in from the right
            val xFinalOffset = xFinalOffsets[view]!!
            val xOffset = ((2.0 - progress) * xFinalOffset).toInt()

            val font = Font(this.font.typeface, fontSize)

            view.alpha = alpha
            TextViewUtilities.setFont(view, font)

            val size = ViewUtilities.intrinsicSize(view)
            val center = Point(containerSize.width / 2 + xOffset, containerSize.height / 2)
            val origin = Point(center.x - size.width / 2, center.y - size.height / 2)

            val layoutParams = view.layoutParams as LayoutParams
            layoutParams.gravity = Gravity.NO_GRAVITY
            layoutParams.leftMargin = origin.x
            layoutParams.topMargin = origin.y
            layoutParams.width = size.width
            layoutParams.height = size.height
            view.layoutParams = layoutParams

            view.forceLayout()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(containerSize.width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(containerSize.height, MeasureSpec.EXACTLY))
    }

    companion object {

        // This is a quadratic function chosen to start at 0, end at 1, and spring up to 2 at 75% progress
        private var easeOutBackCurve: (Double) -> Double = {

            { x ->

                -20.0/3.0*x*x + 23.0/3.0*x
            }
        }()
    }
}