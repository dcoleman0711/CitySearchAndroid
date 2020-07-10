package com.example.citysearch.unit

import android.content.Context
import android.graphics.Rect
import android.util.Size
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.StubMeasureConverter
import com.example.citysearch.reactive.*
import com.example.citysearch.ui.RecyclerCell
import com.example.citysearch.viewmodels.CellData
import com.example.citysearch.viewmodels.CellTapCommand
import com.example.citysearch.viewmodels.RecyclerViewModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecyclerViewBinderTests {

    lateinit var steps: RecyclerViewBinderSteps

    val Given: RecyclerViewBinderSteps get() = steps
    val When: RecyclerViewBinderSteps get() = steps
    val Then: RecyclerViewBinderSteps get() = steps

    @Before
    fun setUp() {

        steps = RecyclerViewBinderSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testBindRecyclerViewCells() {

        val recyclerView = Given.recyclerView()
        val binder = Given.binder()
        val cellUpdate = Given.bindCells(binder, recyclerView)
        val cellViewModels = Given.cellViewModels()
        val cellData = Given.cellData(cellViewModels)
        val viewModel = Given.viewModel(cellData)

        When.updateViewModels(viewModel)

        Then.recyclerViewCellsHaveViewModels(recyclerView, cellViewModels)
    }

    @Test
    fun testBindRecyclerViewSizes() {

        val recyclerView = Given.recyclerView()
        val binder = Given.binder()
        val cellUpdate = Given.bindCells(binder, recyclerView)
        val cellViewModels = Given.cellViewModels()
        val cellData = Given.cellData(cellViewModels)
        val cellSizes = Given.cellSizes(cellData)
        val viewModel = Given.viewModel(cellData)

        When.updateViewModels(viewModel)

        Then.recyclerViewCellsHaveSizes(recyclerView, cellSizes)
    }

    @Test
    fun testBindRecyclerViewItemSpacing() {

        val recyclerView = Given.recyclerView()
        val binder = Given.binder()
        val cellUpdate = Given.bindCells(binder, recyclerView)
        val horSpacing = Given.horSpacing()
        val viewModel = Given.viewModel(horSpacing = horSpacing)

        When.updateViewModels(viewModel)

        Then.recyclerViewHasHorSpacing(recyclerView, horSpacing)
    }

    @Test
    fun testBindRecyclerViewLeftMargin() {

        val recyclerView = Given.recyclerView()
        val binder = Given.binder()
        val cellUpdate = Given.bindCells(binder, recyclerView)
        val horMargins = Given.horMargins();
        val viewModel = Given.viewModel(horMargins = horMargins)

        When.updateViewModels(viewModel)

        Then.recyclerViewHasLeftHorMargin(recyclerView, horMargins)
    }

    @Test
    fun testBindRecyclerViewRightMargin() {

        val recyclerView = Given.recyclerView()
        val binder = Given.binder()
        val cellUpdate = Given.bindCells(binder, recyclerView)
        val horMargins = Given.horMargins();
        val viewModel = Given.viewModel(horMargins = horMargins)

        When.updateViewModels(viewModel)

        Then.recyclerViewHasRightHorMargin(recyclerView, horMargins)
    }

    @Test
    fun testBindRecyclerViewLineSpacing() {

        val recyclerView = Given.recyclerView()
        val binder = Given.binder()
        val cellUpdate = Given.bindCells(binder, recyclerView)
        val verSpacing = Given.verSpacing()
        val viewModel = Given.viewModel(verSpacing = verSpacing)

        When.updateViewModels(viewModel)

        Then.recyclerViewHasVerSpacing(recyclerView, verSpacing)
    }

    @Test
    fun testBindRecyclerViewTapCommands() {

        val recyclerView = Given.recyclerView()
        val binder = Given.binder()
        val cellUpdate = Given.bindCells(binder, recyclerView)
        val cellViewModels = Given.cellViewModels()
        val cellData = Given.cellData(cellViewModels)
        val viewModel = Given.viewModel(cellData)
        val expectedTapCommands = Given.tapCommands(cellData)
        Given.updateViewModels(viewModel)

        val tapCommands = When.tapOnCells(recyclerView)

        Then.tapCommandsAreEqual(tapCommands, expectedTapCommands)
    }
}

class RecyclerViewBinderSteps(private val context: Context) {

    private val displayedCells = ArrayList<TestMVVMCell>()

    private var invokedCommand: CellTapCommand? = null

    // Doesn't test multi-row layouts
    private val layout = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)

    private var adapter: RecyclerViewBindingAdapter<String, TestMVVMCell>? = null

    private val recyclerView = mock<RecyclerView> {

        on { layoutManager }.thenReturn(layout)
    }

    private val cellUpdates = BehaviorSubject.create<RecyclerViewModel<String>>()

    private val measureConverter = StubMeasureConverter.stubMeasureConverter()

    fun recyclerView(): RecyclerView {

        return recyclerView
    }

    fun binder(): RecyclerViewBinderImp<String, TestMVVMCell> {

        return RecyclerViewBinderImp(::TestMVVMCell, measureConverter)
    }

    fun bindCells(binder: RecyclerViewBinderImp<String, TestMVVMCell>, recyclerView: RecyclerView): Disposable {

        doAnswer { invocation ->

            val adapter = invocation.getArgument<RecyclerViewBindingAdapter<String, TestMVVMCell>>(0)
            this.adapter = adapter

            adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {

                override fun onChanged() {

                   captureRecyclerViewCells()
                }
            })

        }.whenever(recyclerView).adapter = any()

        return binder.bindCells(recyclerView, cellUpdates)
    }

    fun cellViewModels(): List<String> {

        return (0 until 5).map( { index -> "StubViewModel #$index" })
    }

    fun cellData(cellViewModels: List<String>): List<CellData<String>> {

        return (0 until cellViewModels.size).map { index ->

            val viewModel = cellViewModels[index]

            val command = mock<CellTapCommand>()
            whenever(command.invoke()).then {
                
                this.invokedCommand = command

                null
            }

            CellData(
                viewModel,
                Size(index * 32, index * 64),
                command
            )

        }
    }

    fun cellSizes(cellData: List<CellData<String>>): List<Size> {

        return cellData.map({ cellDatum -> cellDatum.size })
    }

    fun tapCommands(cellData: List<CellData<String>>): List<CellTapCommand> {

        return cellData.map({ cellDatum -> cellDatum.tapCommand!! })
    }

    fun horSpacing(): Int {

        return 12
    }

    fun verSpacing(): Int {

        return 14
    }

    fun horMargins(): Int {

        return 20
    }

    fun viewModel(cellData: List<CellData<String>> = arrayListOf(), horSpacing: Int = 0, verSpacing: Int = 0, horMargins: Int = 0): RecyclerViewModel<String> {

        return RecyclerViewModel<String>(
            cellData,
            horSpacing,
            verSpacing,
            horMargins
        )
    }

    fun updateViewModels(viewModel: RecyclerViewModel<String>) {

        cellUpdates.onNext(viewModel)
        captureRecyclerViewCells()
    }

    fun tapOnCells(recyclerView: RecyclerView): List<CellTapCommand> {

        return displayedCells.map { cell ->
            
            cell.view.performClick()
            this.invokedCommand ?: mock<CellTapCommand>()
        }
    }

    fun recyclerViewCellsHaveViewModels(recyclerView: RecyclerView, expectedCellData: List<String>) {

        val viewModels = displayedCells.map { cell -> cell.viewModel ?: "" }

        Assert.assertEquals("Recycler view cells are not correct", expectedCellData, viewModels)
    }

    fun recyclerViewCellsHaveSizes(recyclerView: RecyclerView, expectedSizes: List<Size>) {

        val cellPxSizes = displayedCells.map { cell ->

            val layoutParams = cell.view.layoutParams
            Size(layoutParams.width, layoutParams.height)
        }

        val expectedPxSizes = expectedSizes.map { size ->

            Size(measureConverter.convertToPixels(size.width), measureConverter.convertToPixels(size.height))
        }

        Assert.assertEquals("Recycler view cell sizes are not correct", expectedPxSizes, cellPxSizes)
    }

    fun tapCommandsAreEqual(tapCommands: List<CellTapCommand>, expectedTapCommands: List<CellTapCommand>) {

        Assert.assertEquals("Cell tap commands were not invoked correctly", expectedTapCommands, tapCommands)
    }

    fun recyclerViewHasHorSpacing(recyclerView: RecyclerView, expectedSpacing: Int) {

        // Doesn't test multi-row layouts
        val itemDecorationCaptor = argumentCaptor<RecyclerView.ItemDecoration>()
        verify(recyclerView).addItemDecoration(itemDecorationCaptor.capture())

        val itemDecoration = itemDecorationCaptor.firstValue

        val cell = mock<View> { }
        whenever(recyclerView.getChildLayoutPosition(cell)).thenReturn(2)

        val rect = Rect(0, 0, 0, 0)
        itemDecoration.getItemOffsets(rect, cell, recyclerView, RecyclerView.State())

        Assert.assertEquals("Cell spacing is not correct", measureConverter.convertToPixels(expectedSpacing), rect.left)
    }

    fun recyclerViewHasLeftHorMargin(recyclerView: RecyclerView, expectedSpacing: Int) {

        // Doesn't test multi-row layouts
        val itemDecorationCaptor = argumentCaptor<RecyclerView.ItemDecoration>()
        verify(recyclerView).addItemDecoration(itemDecorationCaptor.capture())

        val itemDecoration = itemDecorationCaptor.firstValue

        val cell = mock<View> { }
        whenever(recyclerView.getChildLayoutPosition(cell)).thenReturn(0)

        val rect = Rect(0, 0, 0, 0)
        itemDecoration.getItemOffsets(rect, cell, recyclerView, RecyclerView.State())

        Assert.assertEquals("First cell left space should be horizontal margin", measureConverter.convertToPixels(expectedSpacing), rect.left)
    }

    fun recyclerViewHasRightHorMargin(recyclerView: RecyclerView, expectedSpacing: Int) {

        // Doesn't test multi-row layouts
        val itemDecorationCaptor = argumentCaptor<RecyclerView.ItemDecoration>()
        verify(recyclerView).addItemDecoration(itemDecorationCaptor.capture())

        val itemDecoration = itemDecorationCaptor.firstValue

        val cellCount = 50

        val cell = mock<View> { }
        whenever(recyclerView.childCount).thenReturn(cellCount)
        whenever(recyclerView.getChildLayoutPosition(cell)).thenReturn(cellCount - 1)

        val rect = Rect(0, 0, 0, 0)
        itemDecoration.getItemOffsets(rect, cell, recyclerView, RecyclerView.State())

        Assert.assertEquals("Last cell right space should be horizontal margin", measureConverter.convertToPixels(expectedSpacing), rect.right)
    }

    fun recyclerViewHasVerSpacing(recyclerView: RecyclerView, expectedSpacing: Int) {

        // Doesn't test multi-row layouts
        val itemDecorationCaptor = argumentCaptor<RecyclerView.ItemDecoration>()
        verify(recyclerView).addItemDecoration(itemDecorationCaptor.capture())

        val itemDecoration = itemDecorationCaptor.firstValue

        val cell = mock<View> { }
        whenever(recyclerView.getChildLayoutPosition(cell)).thenReturn(0)

        val rect = Rect(0, 0, 0, 0)
        itemDecoration.getItemOffsets(rect, cell, recyclerView, RecyclerView.State())

        Assert.assertEquals("First cell offset should be 0", 0, rect.top)

        whenever(recyclerView.getChildLayoutPosition(cell)).thenReturn(1)

        itemDecoration.getItemOffsets(rect, cell, recyclerView, RecyclerView.State())

        Assert.assertEquals("Cell spacing is not correct", measureConverter.convertToPixels(expectedSpacing), rect.top)
    }

    private fun captureRecyclerViewCells() {

        val adapter = adapter!!

        displayedCells.clear()

        val parent = FrameLayout(context)

        for(index in 0 until adapter.itemCount) {

            val holder = adapter.onCreateViewHolder(parent, 0)
            adapter.onBindViewHolder(holder, index)
            displayedCells.add(holder.cell as TestMVVMCell)
        }
    }
}

class TestMVVMCell(override val view: View):
    RecyclerCell<String> {

    override var viewModel: String? = null

    constructor(context: Context) : this(View(context))
}