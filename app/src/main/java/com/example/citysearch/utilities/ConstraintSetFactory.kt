package com.example.citysearch.utilities

import androidx.constraintlayout.widget.ConstraintSet

/**
 * This allows capturing and verifying calls to a ConstraintSet, which allows testing the dynamic constraints created in code
 */
interface ConstraintSetFactory {

    fun constraintSet(): ConstraintSet
}

class ConstraintSetFactoryImp: ConstraintSetFactory {

    override fun constraintSet(): ConstraintSet {

        return ConstraintSet()
    }
}