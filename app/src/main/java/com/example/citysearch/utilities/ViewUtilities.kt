package com.example.citysearch.utilities

import android.view.View
import android.widget.TextView

class ViewUtilities {

    companion object {

        // Measures a view's size (aka the "wrap-content" size) and returns it
        fun intrinsicSize(view: View): Size {

            val currentMeasuredWidth = view.getMeasuredWidth()
            val currentMeasuredHeight = view.getMeasuredHeight()

            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

            val result = Size(View.MeasureSpec.getSize(view.measuredWidth), View.MeasureSpec.getSize(view.measuredHeight))

            view.measure(currentMeasuredWidth, currentMeasuredHeight)

            return result;
        }

        // More iOS-y stuff
        fun frame(view: View): Rect {

            return Rect(Point(x = view.left, y = view.top), Size(width = view.width, height = view.height))
        }

        fun center(view: View): Point {

            return frame(view).center
        }

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

// Extends a TextView to work with the Font data type
class TextViewUtilities {

    companion object {

        fun setFont(view: TextView, font: Font) {

            view.typeface = font.typeface
            view.textSize = font.size.toFloat()
        }
    }
}