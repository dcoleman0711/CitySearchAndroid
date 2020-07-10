package com.example.citysearch.unit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.ui.AsyncImageCell
import com.example.citysearch.viewmodels.AsyncImageViewModel
import com.example.citysearch.reactive.ViewBinder
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
}

class AsyncImageCellSteps(private val context: Context) {

    private val view = ConstraintLayout(context)

    private val imageView = ImageView(context)

    private val viewModelImage = BehaviorSubject.create<Optional<Bitmap>>()

    private val viewModel = mock<AsyncImageViewModel> {

        on { image }.thenReturn(viewModelImage)
    }

    private val binder = mock<ViewBinder> {  }

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

        return AsyncImageCell(view, imageView, binder)
    }

    fun assignViewModelToCell(viewModel: AsyncImageViewModel, cell: AsyncImageCell) {

        cell.viewModel = viewModel
    }

    fun imageViewIsBoundToViewModel(imageView: ImageView, viewModel: AsyncImageViewModel) {

        verify(binder).bindImageView(imageView, viewModelImage)
    }
}