package com.example.citysearch.unit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageCell
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModel
import com.example.citysearch.reactive.ViewBinder
import com.example.citysearch.utilities.ConstraintSetFactory
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AsyncImageCellTests {

    lateinit var steps: AsyncImageCellSteps

    val Given: AsyncImageCellSteps get() = steps
    val When: AsyncImageCellSteps get() = steps
    val Then: AsyncImageCellSteps get() = steps

    @Before
    fun setUp() {
        
        steps = AsyncImageCellSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testImageViewImage() {

        val viewModel = Given.viewModel()
        val imageView = Given.imageView()
        val binder = Given.binder()
        val imageCell = Given.imageCellIsCreated(imageView = imageView, binder = binder)

        When.assignViewModelToCell(viewModel, imageCell)

        Then.imageViewIsBoundToViewModel(imageView, viewModel)
    }

    @Test
    fun testImageViewConstraints() {

        val imageView = Given.imageView()
        val imageCell = When.imageCellIsCreated(imageView = imageView)

        Then.imageViewFillsParent(imageView, imageCell)
    }
}

class AsyncImageCellSteps(private val context: Context) {

    private val imageView = ImageView(context)

    private val viewModelImage = BehaviorSubject.create<Optional<Bitmap>>()

    private val viewModel = mock<AsyncImageViewModel> {

        on { image }.thenReturn(viewModelImage)
    }

    private val binder = mock<ViewBinder> {  }

    private val constraints = mock<ConstraintSet> {  }

    private val constraintSetFactory = mock<ConstraintSetFactory> {

        on { constraintSet() }.thenReturn(constraints)
    }

    fun binder(): ViewBinder {
        
        return binder
    }

    fun image(): Bitmap {

        val fileStream = javaClass.classLoader!!.getResourceAsStream("assets/TestImage.jpg")
        return BitmapFactory.decodeStream(fileStream)
    }

    fun imageView(): ImageView {

        return imageView
    }

    fun viewModel(): AsyncImageViewModel {

        return viewModel
    }

    fun imageCellIsCreated(imageView: ImageView = this.imageView, binder: ViewBinder = this.binder): AsyncImageCell {

        return AsyncImageCell(context, imageView, binder, constraintSetFactory)
    }

    fun assignViewModelToCell(viewModel: AsyncImageViewModel, cell: AsyncImageCell) {

        cell.viewModel = viewModel
    }

    fun imageViewIsBoundToViewModel(imageView: ImageView, viewModel: AsyncImageViewModel) {

        verify(binder).bindImageView(imageView, viewModelImage)
    }

    fun imageViewFillsParent(imageView: ImageView, cell: AsyncImageCell) {

        verify(constraints, atLeastOnce()).connect(imageView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        verify(constraints, atLeastOnce()).connect(imageView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        verify(constraints, atLeastOnce()).connect(imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        verify(constraints, atLeastOnce()).connect(imageView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
    }
}