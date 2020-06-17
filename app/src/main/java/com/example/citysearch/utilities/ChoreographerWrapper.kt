package com.example.citysearch.utilities

import android.view.Choreographer

interface ChoreographerWrapper {

    fun postFrameCallback(callback: Choreographer.FrameCallback)
}

class ChoreographerWrapperImp: ChoreographerWrapper {

    private val choreographer = Choreographer.getInstance()

    override fun postFrameCallback(callback: Choreographer.FrameCallback) {

        choreographer.postFrameCallback(callback)
    }
}