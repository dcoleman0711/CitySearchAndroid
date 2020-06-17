package com.example.citysearch.reactive

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citysearch.utilities.MeasureConverter
import com.example.citysearch.utilities.MeasureConverterImp
import com.example.citysearch.utilities.ViewUtilities
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface  RecyclerViewBinder<ViewModel, CellType: RecyclerCell<ViewModel>> {

    fun bindCells(view: RecyclerView, viewModelUpdates: Observable<RecyclerViewModel<ViewModel>>): Disposable
}

class RecyclerViewBinderImp<ViewModel, CellType: RecyclerCell<ViewModel>>(private val context: Context, private val cellConstructor: (Context) -> CellType): RecyclerViewBinder<ViewModel, CellType> {

    override fun bindCells(view: RecyclerView, viewModelUpdates: Observable<RecyclerViewModel<ViewModel>>): Disposable {

        val adapter = RecyclerViewBindingAdapter(context, cellConstructor)
        view.adapter = adapter

        return viewModelUpdates.subscribe({ viewModel ->

            for(decIndex in 0 until view.itemDecorationCount)
                view.removeItemDecorationAt(decIndex)

            val spacingDecoration = SpacingDecoration<ViewModel>(viewModel.horSpacing, viewModel.verSpacing, context)
            view.addItemDecoration(spacingDecoration)

            adapter.cellData = viewModel.cells
            adapter.notifyDataSetChanged()

        }, { error ->

            print(error)
        })
    }
}

class RecyclerViewBindingAdapter<ViewModel, CellType: RecyclerCell<ViewModel>>(private val constructor: (Context) -> CellType, private val measureConverter: MeasureConverter): RecyclerView.Adapter<RecyclerViewBindingAdapter<ViewModel, CellType>.CellHolder>() {

    constructor(context: Context, constructor: (Context) -> CellType) : this(constructor, MeasureConverterImp(context))

    inner class CellHolder(val cell: RecyclerCell<ViewModel>): RecyclerView.ViewHolder(cell) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellHolder {

        val view = constructor(parent.context)
        return CellHolder(view)
    }

    override fun getItemCount(): Int {

        return cellData.size
    }

    override fun onBindViewHolder(holder: CellHolder, position: Int) {

        val cellData = cellData[position]

        val cell = holder.cell
        val viewModel = cellData.viewModel
        cell.viewModel = viewModel

        cell.setOnClickListener({ cellData.tapCommand?.invoke() })

        val layoutParams = cell.getLayoutParams() as? RecyclerView.LayoutParams ?: RecyclerView.LayoutParams(0, 0)
        layoutParams.width = measureConverter.convertToPixels(cellData.size.width)
        layoutParams.height = measureConverter.convertToPixels(cellData.size.height)
        cell.setLayoutParams(layoutParams)
    }

    var cellData: Array<CellData<ViewModel>> = arrayOf()
}

class SpacingDecoration<ViewModel>(private val horSpacing: Int, private val verSpacing: Int, private val measureConverter: MeasureConverter): RecyclerView.ItemDecoration() {

    constructor(horSpacing: Int, verSpacing: Int, context: Context) : this(horSpacing, verSpacing, MeasureConverterImp(context))

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        // This obviously only works for horizontally scrolling grid layouts
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val index = parent.getChildLayoutPosition(view)
        val column = index / spanCount
        val row = index % spanCount

        if(column > 0)
            outRect.left = measureConverter.convertToPixels(horSpacing)

        if(row > 0)
            outRect.top = measureConverter.convertToPixels(verSpacing)
    }
}