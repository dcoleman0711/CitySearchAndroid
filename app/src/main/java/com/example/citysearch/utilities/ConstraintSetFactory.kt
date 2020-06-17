package com.example.citysearch.utilities

import androidx.constraintlayout.widget.ConstraintSet

interface ConstraintSetFactory {

    fun constraintSet(): ConstraintSet
}

class ConstraintSetFactoryImp: ConstraintSetFactory {

    override fun constraintSet(): ConstraintSet {

        return ConstraintSet()
    }
}