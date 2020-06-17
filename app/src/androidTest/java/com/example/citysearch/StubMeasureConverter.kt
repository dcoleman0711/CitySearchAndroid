package com.example.citysearch

import com.example.citysearch.utilities.MeasureConverter
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock

class StubMeasureConverter {

    companion object {

        fun stubMeasureConverter(): MeasureConverter {

            return mock<MeasureConverter> {

                on { convertToPixels(any()) }.then { invocation ->

                    val dp = invocation.getArgument<Int>(0)

                    dp * 4
                }
            }
        }
    }
}