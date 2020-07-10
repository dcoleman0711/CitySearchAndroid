package com.example.citysearch.utilities

import android.view.Choreographer

/**
 * Choreographer is final, so this is introduced to make interactions with it testable
 */
interface ChoreographerWrapper {

    fun postFrameCallback(callback: Choreographer.FrameCallback)
}

class ChoreographerWrapperImp: ChoreographerWrapper {

    private val choreographer = Choreographer.getInstance()

    override fun postFrameCallback(callback: Choreographer.FrameCallback) {

        choreographer.postFrameCallback(callback)
    }
}