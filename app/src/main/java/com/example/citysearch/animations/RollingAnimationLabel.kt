package com.example.citysearch.animations

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Choreographer
import android.view.View
import android.view.textclassifier.TextLanguage
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.utilities.*
import java.time.Duration
import java.time.Instant
import java.util.function.BiConsumer
import java.util.function.Supplier

open class RollingAnimationLabel(context: Context): AppCompatTextView(context) {

//    private var text: String = ""
//    private var font: Font = Font(Typeface.DEFAULT, 12.0)

    // These can easily be exposed for configurability
    private var animationCurve: (Double) -> Double = RollingAnimationLabel.easeOutBackCurve
    private var totalDuration: Duration = Duration.ofSeconds(4)
    private var characterDuration: Duration = Duration.ofSeconds(1)

    private var characterViews: Array<TextView> = arrayOf<TextView>()

    private lateinit var choreographer: Choreographer

    private var startTime: Long? = null

    private var startTimes: Map<TextView, Instant> = mapOf<TextView, Instant>()
    private var characterConstraints: Map<TextView, ConstraintSet> = mapOf<TextView, ConstraintSet>()
    private var xFinalOffsets: Map<TextView, Int> = mapOf<TextView, Int>()

    private var running = false

    fun start(text: String, font: Font) {

        this.setTextColor(Color.BLACK)
        this.text = text
//        this.font = font

        TextViewUtilities.setFont(this, font)
//        this.animationCurve = RollingAnimationLabel.easeOutBackCurve
//
//        // These can be easily exposed for configurability
//        this.totalDuration = Duration.ofSeconds(4)
//        this.characterDuration = Duration.ofSeconds(1)
//
//        running = true
//
//        this.choreographer = Choreographer.getInstance()
//
//        construct()
//
//        pumpChoreographer()
    }

    private fun pumpChoreographer() {

        choreographer.postFrameCallback { frameTimeNanos ->

            tick(frameTimeNanos)

            if (running) { pumpChoreographer() }
        }
    }

    private fun construct() {

        // Break the label up into one label for each character
//        val characters = text.toCharArray()
//
//        this.characterViews = characters.map({ char -> characterView(char) }).toTypedArray()
//
//        // Use sizeThatFits to get the size of each character label, then accumulate the sizes to build frames left-to-right
//        val sizes = characterViews.map { characterView -> ViewUtilities.intrinsicSize(characterView) }
//
//        val frames = sizes.map { size -> Rect(origin = Point.zero, size = size) }.stream()
//            .collect(::ArrayList, BiConsumer<ArrayList<Rect>, Rect> { sums, next ->
//
//            var x = 0
//            if(!sums.isEmpty()) {
//                x = sums.last().maxX
//            }
//
//            sums.add(Rect(origin = Point(x = x, y = 0), size = next.size))
//
//        }, ArrayList<Rect>::addAll)
//
//        // Now get the center of each character
//        val centers = frames.map { frame -> frame.center }
//        val viewsAndCenters = CollectionUtilities.zip(characterViews, centers)
//
//        // The width of the entire label is the right edge of the last frame
//        val width = frames.lastOrNull()?.maxX ?: 0
//        val height = this.font.size.toInt()
//
//        val containerCenter = Point(x = width / 2, y = height / 2)
//
//        // Build constraints from the center of each label's calculated frame to the center of the container.  The center will be the "anchor" during the animation
//        val xCharacterConstraints = viewsAndCenters.map { view, center ->
//
//            val constraint = ConstraintSet()
//            constraint.centerHorizontally()
//            view.centerXAnchor.constraint(equalTo: this.centerXAnchor, constant: center.x - containerCenter.x)
//        }
//        this.characterConstraints = [UILabel: NSLayoutConstraint](uniqueKeysWithValues: zip(characterViews, xCharacterConstraints))
//
//        // Store the constant of each constraint for its "actual" position in the label, which is where it will be at the end of the animation
//        this.xFinalOffsets = [UILabel: Float](uniqueKeysWithValues: zip(characterViews, xCharacterConstraints.map { constraint in constraint.constant }))
//
//        val yCharacterConstraints = viewsAndCenters.map { view, center in view.centerYAnchor.constraint(equalTo: this.centerYAnchor, constant: center.y - containerCenter.y) }
//
//        val allConstraints = [NSLayoutConstraint]([xCharacterConstraints, yCharacterConstraints].joined())
//
//        this.addConstraints(allConstraints)
//
//        val sizeConstraints = [this.widthAnchor.constraint(equalToConstant: width),
//                this.heightAnchor.constraint(equalToConstant: height)]
//
//        this.addConstraints(sizeConstraints)
//
//        // No need to animate whitespace character, no one would know the difference!
//        val nonwhiteSpaceCharacterViews = characterViews.filter { label in !label.text!.first!.isWhitespace }
//
//        // The last character's animation should end at the end of the total duration.  Therefore the last character should begin its animation at this duration minus the individual character duration.  The start times are then evenly spread between 0 and this maximum interval
//        val lastAnimationStart = totalDuration - characterDuration
//        val lastIndex = nonwhiteSpaceCharacterViews.count - 1
//        val startTimes = (0...lastIndex).map { index in CFTimeInterval(index) * CFTimeInterval(lastAnimationStart) / CFTimeInterval(lastIndex) }
//
//        this.startTimes = [UILabel: CFTimeInterval](uniqueKeysWithValues: zip(nonwhiteSpaceCharacterViews, startTimes))
    }

    private fun characterView(character: Char): TextView {

        val characterView = TextView(this.context)
        characterView.text = character.toString()
//        TextViewUtilities.setFont(characterView, font)

//        this.addSubview(characterView)

        return characterView
    }

    private fun tick(frameTimeNanos: Long) {

        // Choreographer returns uptime, and we need a point of reference, so we have to let one frame go by to define the start frame
        if(startTime == null) {
            this.startTime = frameTimeNanos
            return
        }

        val offset = Duration.ofNanos(frameTimeNanos - startTime!!)

        if(offset > totalDuration) {

            // The animation is compvaled.  Remove the display link so it doesn't keep this object alive
            running = false
            return
        }

        // Map each offset within the duration to the offset relative to the current time.  Then scale by the character duration, and we get an array of progresses (from 0 to 1) of each character animation
//        val characterOffsets = this.startTimes.mapValues { startTime in offset - startTime }
//        val characterProgresses = characterOffsets.mapValues { offset in min(max(offset / this.characterDuration, 0.0), 1.0) }
//
//        for (view, progress) in characterProgresses {
//
//            // The animation consists of three parts: a linear alpha fade-in, a quadratic font scaling, and a reverse linear approach into the final position.
//            val alpha = Float(progress)
//            val fontSize = this.font.pointSize * Float(animationCurve(progress))
//
//            // The constraint constants will be scaled, starting at twice their final value.  The reason we picked the center of the container as the anchor is so that the characters on the left come in from the left, and the characters on the right come in from the right
//            val xFinalOffset = xFinalOffsets[view]!
//            val xOffset = Float(2.0 - progress) * xFinalOffset
//            characterConstraints[view]!.constant = xOffset
//
//            // One way to fake font scaling is with transforms, but this gives better results, because it actually transitions smoothly through each font size
//            val font = this.font.withSize(fontSize)
//
//            view.alpha = alpha
//            view.font = font
//        }
//
//        this.setNeedsLayout()
//        this.layoutIfNeeded()
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