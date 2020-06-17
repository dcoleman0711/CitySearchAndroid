package com.example.citysearch.search.searchresults.citysearchresultcell

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import com.example.citysearch.R
import com.example.citysearch.reactive.RecyclerCell
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.reactive.ViewBinderImp
import com.example.citysearch.utilities.MeasureConverter
import com.example.citysearch.utilities.MeasureConverterImp
import com.example.citysearch.utilities.ViewUtilities
import io.reactivex.disposables.Disposable
import java.util.*

class CitySearchResultCell(context: Context,
                           private val titleLabel: TextView,
                           private val imageView: ImageView,
                           private val measureConverter: MeasureConverter): RecyclerCell<CitySearchResultViewModel>(context) {

    constructor(context: Context) : this(context, TextView(context), ImageView(context), MeasureConverterImp(context))

    override var viewModel: CitySearchResultViewModel? = null
    set(value) {

        field = value

        unbind()

        if(value == null)
            return

        bindToViewModel(value)
    }

    init {

        setupView()
        buildLayout()
    }

    private fun setupView() {

        imageView.setBackgroundColor(Color.GREEN)
        this.id = R.id.view

        titleLabel.id = R.id.titleLabel
        val titleBackground = GradientDrawable()
        titleBackground.setColor(Color.argb(0.8f, 1.0f, 1.0f, 1.0f))
        titleBackground.setStroke(measureConverter.convertToPixels(1), Color.BLACK)
        titleBackground.cornerRadius = measureConverter.convertToPixels(8).toFloat()
        titleLabel.background = titleBackground
        titleLabel.gravity = Gravity.CENTER
        addView(titleLabel)

        imageView.id = R.id.imageView
        val imageViewBackground = GradientDrawable()
        imageViewBackground.setColor(Color.argb(0.2f, 0.0f, 0.0f, 0.0f))
        imageViewBackground.cornerRadius = measureConverter.convertToPixels(52).toFloat()
        imageView.background = imageViewBackground
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.clipToOutline = true
        addView(imageView)
    }

    private fun buildLayout() {

        val constraints = ConstraintSet()

        // TitleLabel
        constraints.connect(titleLabel.id, ConstraintSet.LEFT, this.id, ConstraintSet.LEFT)
        constraints.connect(titleLabel.id, ConstraintSet.RIGHT, this.id, ConstraintSet.RIGHT)
        constraints.connect(titleLabel.id, ConstraintSet.BOTTOM, this.id, ConstraintSet.BOTTOM)
        constraints.constrainHeight(titleLabel.id, View.MeasureSpec.makeMeasureSpec(measureConverter.convertToPixels(24), MeasureSpec.EXACTLY))

        // ImageView
        constraints.connect(imageView.id, ConstraintSet.LEFT, this.id, ConstraintSet.LEFT)
        constraints.connect(imageView.id, ConstraintSet.RIGHT, this.id, ConstraintSet.RIGHT)
        constraints.connect(imageView.id, ConstraintSet.TOP, this.id, ConstraintSet.TOP)
        constraints.connect(imageView.id, ConstraintSet.BOTTOM, titleLabel.id, ConstraintSet.TOP)
        constraints.setDimensionRatio(imageView.id, "W,1:1")

        constraints.applyTo(this)
    }

    private fun bindToViewModel(viewModel: CitySearchResultViewModel) {

        titleBinding = binder.bindTextView(titleLabel, viewModel.title)
        imageBinding = binder.bindImageView(imageView, viewModel.iconImage.map { image -> Optional.of(image) })
    }

    private fun unbind() {

        titleBinding?.dispose()
        titleBinding = null

        imageBinding?.dispose()
        imageBinding = null
    }

    private val binder = ViewBinderImp()

    private var titleBinding: Disposable? = null
    private var imageBinding: Disposable? = null
}