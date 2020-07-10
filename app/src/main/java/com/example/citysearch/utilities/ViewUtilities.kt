package com.example.citysearch.utilities

import android.util.Size
import android.view.View
import android.widget.TextView

/**
 * Provides some useful utility functions for Views
 */
class ViewUtilities {

    companion object {

        /**
         * Measures a view's size (aka the "wrap-content" size) and returns it
         */
        fun intrinsicSize(view: View): Size {

            val currentMeasuredWidth = view.measuredWidth
            val currentMeasuredHeight = view.measuredHeight

            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    0,
                    View.MeasureSpec.UNSPECIFIED
                ), View.MeasureSpec.makeMeasureSpec(
                    0,
                    View.MeasureSpec.UNSPECIFIED
                )
            )

            val result = Size(
                View.MeasureSpec.getSize(view.measuredWidth),
                View.MeasureSpec.getSize(view.measuredHeight)
            )

            view.measure(currentMeasuredWidth, currentMeasuredHeight)

            return result;
        }

        /**
         * Determines whether a view is in the hierarchy of a parent
         */
        fun isDescendantOf(child: View, parent: View): Boolean {

            var currentParent: View?

            do {

                currentParent = child.parent as? View
                if(currentParent == parent)
                    return true;

            } while(currentParent != null)

            return false;
        }
    }
}

/**
 * Extends a TextView to work with the Font data type
 */
class TextViewUtilities {

    companion object {

        fun setFont(view: TextView, font: Font) {

            view.typeface = font.typeface
            view.textSize = font.size.toFloat()
        }
    }
}