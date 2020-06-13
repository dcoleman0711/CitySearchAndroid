package com.example.citysearch.reactive

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citysearch.utilities.ViewUtilities
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class RecyclerViewBinder<ViewModel, CellType: RecyclerCell<ViewModel>>(private val cellConstructor: (Context) -> CellType) {

    fun bindCells(view: RecyclerView, viewModelUpdates: Observable<RecyclerViewModel<ViewModel>>): Disposable {

        val adapter = RecyclerViewBindingAdapter(cellConstructor)
        view.adapter = adapter

        return viewModelUpdates.subscribe { viewModel ->

            for(decIndex in 0..view.itemDecorationCount-1) {
                view.removeItemDecorationAt(decIndex)
            }

            val spacingDecoration = SpacingDecoration<ViewModel>(viewModel.horSpacing, viewModel.verSpacing)
            view.addItemDecoration(spacingDecoration)

            adapter.cellData = viewModel.cells
            adapter.notifyDataSetChanged()
        }
    }
}

class RecyclerViewBindingAdapter<ViewModel, CellType: RecyclerCell<ViewModel>>(private val constructor: (Context) -> CellType): RecyclerView.Adapter<RecyclerViewBindingAdapter<ViewModel, CellType>.CellHolder>() {

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

        val layoutParams = cell.getLayoutParams() as? RecyclerView.LayoutParams ?: RecyclerView.LayoutParams(0, 0)
        layoutParams.width = ViewUtilities.convertToPixels(cell.context, cellData.size.width)
        layoutParams.height = ViewUtilities.convertToPixels(cell.context, cellData.size.height)
        cell.setLayoutParams(layoutParams)
    }

    var cellData: Array<CellData<ViewModel>> = arrayOf()
}

class SpacingDecoration<ViewModel>(private val horSpacing: Int, private val verSpacing: Int): RecyclerView.ItemDecoration() {

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
            outRect.left = ViewUtilities.convertToPixels(view.context, horSpacing)

        if(row > 0)
            outRect.top = ViewUtilities.convertToPixels(view.context, verSpacing)
    }
}