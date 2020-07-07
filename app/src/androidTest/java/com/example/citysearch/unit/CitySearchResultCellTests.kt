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
import androidx.constraintlayout.widget.ConstraintLayout
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

        steps = CitySearchResultCellSteps(InstrumentationRegistry.getInstrumentation().targetContext)
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
    fun testImageViewData() {

        val viewModel = Given.viewModel()
        val imageView = Given.imageView()
        val binder = Given.binder()
        val searchResultCell = Given.searchResultCellIsCreated(imageView = imageView, binder = binder)

        When.assignViewModelToCell(viewModel, searchResultCell)

        Then.imageViewIsBoundToViewModel(imageView, viewModel)
    }
}

class CitySearchResultCellSteps(private val context: Context) {

    private val view = ConstraintLayout(context)

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

    fun binder(): ViewBinder {
        
        return binder
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

        return CitySearchResultCell(view, titleLabel, imageView, binder)
    }

    fun assignViewModelToCell(viewModel: CitySearchResultViewModel, cell: CitySearchResultCell) {

        cell.viewModel = viewModel
    }

    fun titleLabelIsBoundToViewModel(label: TextView, viewModel: CitySearchResultViewModel) {

        verify(binder).bindTextView(label, viewModelTitle)
    }

    fun imageViewIsBoundToViewModel(imageView: ImageView, viewModel: CitySearchResultViewModel) {

        verify(binder).bindImageView(imageView, viewModelIconImage)
    }
}