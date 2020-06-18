package com.example.citysearch.unit

import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.details.imagecarousel.ImageCarouselViewImp
import com.example.citysearch.details.imagecarousel.ImageCarouselViewModel
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageCell
import com.example.citysearch.details.imagecarousel.asyncimage.AsyncImageViewModel
import com.example.citysearch.reactive.RecyclerViewBinder
import com.example.citysearch.reactive.RecyclerViewModel
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageCarouselViewTests {

    lateinit var steps: ImageCarouselViewSteps

    val Given: ImageCarouselViewSteps get() = steps
    val When: ImageCarouselViewSteps get() = steps
    val Then: ImageCarouselViewSteps get() = steps

    @Before
    fun setUp() {

        steps = ImageCarouselViewSteps()
    }

    @Test
    fun testLayoutIsHorizontalFlow() {

        val recyclerView = Given.recyclerView()

        val view = When.imageCarouselViewIsCreated(recyclerView = recyclerView)

        Then.recyclerViewLayoutIsHorizontalGrid(recyclerView)
    }

    @Test
    fun testBackgroundColorIsClear() {

        val recyclerView = Given.recyclerView()

        val view = When.imageCarouselViewIsCreated(recyclerView = recyclerView)

        Then.recyclerViewBackgroundColorIsClear(recyclerView)
    }

    @Test
    fun testBindCollectionViewCells() {

        val recyclerView = Given.recyclerView()
        val viewModel = Given.imageCarouselViewModel()
        val binder = Given.viewBinder()

        val view = When.imageCarouselViewIsCreated(recyclerView = recyclerView, viewModel = viewModel, binder = binder)

        Then.recyclerViewCellsAreBoundToViewModel(recyclerView, viewModel)
    }
}

class ImageCarouselViewSteps {

    private val recyclerView = mock<RecyclerView> {  }

    private val viewModelCells = BehaviorSubject.create<RecyclerViewModel<AsyncImageViewModel>>()

    private val viewModel = mock<ImageCarouselViewModel> {

        on { results }.thenReturn(viewModelCells)
    }

    private val viewBinder = mock<RecyclerViewBinder<AsyncImageViewModel, AsyncImageCell>> {  }
    
    fun recyclerView(): RecyclerView {

        return recyclerView
    }

    fun imageCarouselViewModel(): ImageCarouselViewModel {

        return viewModel
    }

    fun viewBinder(): RecyclerViewBinder<AsyncImageViewModel, AsyncImageCell> {

       return viewBinder
    }

    fun imageCarouselViewIsCreated(recyclerView: RecyclerView, viewModel: ImageCarouselViewModel = this.viewModel, binder: RecyclerViewBinder<AsyncImageViewModel, AsyncImageCell> = this.viewBinder): ImageCarouselViewImp {

        return ImageCarouselViewImp(recyclerView, viewModel, binder)
    }

    fun recyclerViewLayoutIsHorizontalGrid(recyclerView: RecyclerView) {

        val argumentCaptor = argumentCaptor<RecyclerView.LayoutManager>()
        verify(recyclerView).layoutManager = argumentCaptor.capture()
        
        val gridLayoutManager = argumentCaptor.firstValue as? GridLayoutManager
        if(gridLayoutManager == null) {
            Assert.fail("Recycler view layout is not a flow layout")
            return
        }

        Assert.assertEquals("Recycler view layout flow direction is not horizontal", GridLayoutManager.HORIZONTAL, gridLayoutManager.orientation)
    }

    fun recyclerViewBackgroundColorIsClear(recyclerView: RecyclerView) {

        verify(recyclerView).setBackgroundColor(Color.TRANSPARENT)
    }

    fun recyclerViewCellsAreBoundToViewModel(recyclerView: RecyclerView, viewModel: ImageCarouselViewModel) {

        verify(viewBinder).bindCells(recyclerView, viewModelCells)
    }
}