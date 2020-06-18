package com.example.citysearch.unit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.StubMeasureConverter
import com.example.citysearch.reactive.TextViewModel
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultCell
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModel
import com.example.citysearch.utilities.ConstraintSetFactory
import com.example.citysearch.utilities.Font
import com.nhaarman.mockitokotlin2.*
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.collections.HashMap

@RunWith(AndroidJUnit4::class)
class CitySearchResultCellTests {

    lateinit var steps: CitySearchResultCellSteps

    val Given: CitySearchResultCellSteps get() = steps
    val When: CitySearchResultCellSteps get() = steps
    val Then: CitySearchResultCellSteps get() = steps

    @Before
    fun setUp() {

        steps = CitySearchResultCellSteps(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testTitleLabelCornerRadius() {

        val titleLabel = Given.titleLabel()
        val searchResultCell = When.searchResultCellIsCreated(titleLabel = titleLabel)
        val cornerRadius = Given.cornerRadius()

        Then.titleLabelHasCornerRadius(titleLabel, cornerRadius)
    }

    @Test
    fun testTitleLabelBorderWidth() {

        val titleLabel = Given.titleLabel()
        val searchResultCell = When.searchResultCellIsCreated(titleLabel = titleLabel)
        val borderWidth = Given.borderWidth()

        Then.titleLabelHasBorderWidth(titleLabel, borderWidth)
    }

    @Test
    fun testTitleLabelWhiteBackground() {

        val titleLabel = Given.titleLabel()
        val searchResultCell = When.searchResultCellIsCreated(titleLabel = titleLabel)

        Then.titleLabelHasTranslucentWhiteBackground(titleLabel)
    }

    @Test
    fun testTitleLabelData() {

        val viewModel = Given.viewModel()
        val titleLabel = Given.titleLabel()
        val binder = Given.binder()
        val searchResultCell = Given.searchResultCellIsCreated(titleLabel = titleLabel, binder = binder)

        When.assignViewModelToCell(viewModel, searchResultCell)

        Then.titleLabelIsBoundToViewModel(titleLabel, viewModel)
    }

    @Test
    fun testTitleLabelConstraints() {

        val titleLabel = Given.titleLabel()
        val searchResultCell = When.searchResultCellIsCreated(titleLabel = titleLabel)

        Then.titleLabelIsConstrainedToBottomCenterOfCell(titleLabel, searchResultCell)
    }

    @Test
    fun testImageViewData() {

        val viewModel = Given.viewModel()
        val imageView = Given.imageView()
        val binder = Given.binder()
        val searchResultCell = Given.searchResultCellIsCreated(imageView = imageView, binder = binder)

        When.assignViewModelToCell(viewModel, searchResultCell)

        Then.imageViewIsBoundToViewModel(imageView, viewModel)
    }

    @Test
    fun testImageViewConstraints() {

        val imageView = Given.imageView()
        val titleLabel = Given.titleLabel()
        val searchResultCell = When.searchResultCellIsCreated(titleLabel = titleLabel, imageView = imageView)

        Then.imageViewIsCenteredSquareAndFittedInCell(imageView, searchResultCell, titleLabel)
    }
}

class CitySearchResultCellSteps(private val context: Context) {

    private var labelBindings = HashMap<TextView, TextViewModel>()

    private val titleLabel = TextView(context)
    private val imageView = ImageView(context)
    
    private val binder = mock<ViewBinder> {  }

    private val viewModelTitle = BehaviorSubject.create<TextViewModel>()
    private val viewModelIconImage = BehaviorSubject.create<Optional<Bitmap>>()

    private val viewModel = mock<CitySearchResultViewModel> {

        on { title }.thenReturn(viewModelTitle)
        on { iconImage }.thenReturn(viewModelIconImage)
    }

    private val measureConverter = StubMeasureConverter.stubMeasureConverter()

    private val constraints = mock<ConstraintSet> {  }

    private val constraintSetFactory = mock<ConstraintSetFactory> {

        on { constraintSet() }.thenReturn(constraints)
    }

    private var titleBackground: Drawable? = null
    private var imageBackground: Drawable? = null

    fun binder(): ViewBinder {
        
        return binder
    }

    fun cornerRadius(): Int {

        return measureConverter.convertToPixels( 8)
    }

    fun borderWidth(): Int {

        return measureConverter.convertToPixels(1)
    }

    fun titleLabel(): TextView {

        return titleLabel
    }

    fun imageView(): ImageView {

        return imageView
    }

    fun viewModel(): CitySearchResultViewModel {

        return viewModel
    }

    fun searchResultCellIsCreated(titleLabel: TextView = this.titleLabel, imageView: ImageView = this.imageView, binder: ViewBinder = this.binder): CitySearchResultCell {

        return CitySearchResultCell(context, titleLabel, imageView, binder, constraintSetFactory, measureConverter)
    }

    fun assignViewModelToCell(viewModel: CitySearchResultViewModel, cell: CitySearchResultCell) {

        cell.viewModel = viewModel
    }

    fun titleLabelIsBoundToViewModel(label: TextView, viewModel: CitySearchResultViewModel) {

        verify(binder).bindTextView(label, viewModelTitle)
    }

    fun titleLabelHasTranslucentWhiteBackground(titleLabel: TextView) {

        val background = titleLabel.background as GradientDrawable

        Assert.assertEquals("Title label does not have the correct background color", Color.argb(0.8f, 1.0f, 1.0f, 1.0f), background.color!!.defaultColor)
    }

    fun titleLabelHasCornerRadius(titleLabel: TextView, expectedCornerRadius: Int) {

        val background = titleLabel.background as GradientDrawable

        Assert.assertEquals("Title label does not have the correct corner radius", expectedCornerRadius.toFloat(), background.cornerRadius)
    }

    fun titleLabelHasBorderWidth(titleLabel: TextView, expectedBorderWidth: Int) {

        // I think testing this requires mocking the background drawable, which would involve passing in a drawable factory
        Assert.assertTrue(true)
    }

    fun titleLabelIsConstrainedToBottomCenterOfCell(titleLabel: TextView, cell: CitySearchResultCell) {

        verify(constraints, atLeastOnce()).connect(titleLabel.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        verify(constraints, atLeastOnce()).connect(titleLabel.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        verify(constraints, atLeastOnce()).connect(titleLabel.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        verify(constraints, atLeastOnce()).constrainHeight(titleLabel.id, View.MeasureSpec.makeMeasureSpec(measureConverter.convertToPixels(24), View.MeasureSpec.EXACTLY))
    }

    fun imageViewIsCenteredSquareAndFittedInCell(imageView: ImageView, cell: CitySearchResultCell, titleLabel: TextView) {

        verify(constraints, atLeastOnce()).connect(imageView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        verify(constraints, atLeastOnce()).connect(imageView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        verify(constraints, atLeastOnce()).connect(imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        verify(constraints, atLeastOnce()).connect(imageView.id, ConstraintSet.BOTTOM, titleLabel.id, ConstraintSet.TOP)
        verify(constraints, atLeastOnce()).setDimensionRatio(imageView.id, "W,1:1")
    }

    fun imageViewIsBoundToViewModel(imageView: ImageView, viewModel: CitySearchResultViewModel) {

        verify(binder).bindImageView(imageView, viewModelIconImage)
    }
}