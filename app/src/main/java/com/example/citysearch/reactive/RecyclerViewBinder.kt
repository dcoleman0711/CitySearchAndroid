package com.example.citysearch.reactive

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.citysearch.ui.RecyclerCell
import com.example.citysearch.utilities.MeasureConverter
import com.example.citysearch.utilities.MeasureConverterImp
import com.example.citysearch.viewmodels.CellData
import com.example.citysearch.viewmodels.RecyclerViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Handles binding of a RecyclerView to a stream of RecyclerViewModels.
 */
interface  RecyclerViewBinder<ViewModel, CellType: RecyclerCell<ViewModel>> {

    fun bindCells(
        view: RecyclerView,
        viewModelUpdates: Observable<RecyclerViewModel<ViewModel>>
    ): Disposable
}

class RecyclerViewBinderImp<ViewModel, CellType: RecyclerCell<ViewModel>>(
    private val cellConstructor: (Context) -> CellType,
    private val measureConverter: MeasureConverter
): RecyclerViewBinder<ViewModel, CellType> {

    constructor(
        context: Context,
        constructor: (Context) -> CellType
    ) : this(constructor, MeasureConverterImp(context))

    constructor(
        context: Context,
        layout: Int,
        cellConstructor: (View) -> CellType
    ) : this(context, { ctx ->

        val view = LayoutInflater.from(ctx).inflate(layout, null)
        cellConstructor(view)
    })

    override fun bindCells(
        view: RecyclerView,
        viewModelUpdates: Observable<RecyclerViewModel<ViewModel>>
    ): Disposable {

        val adapter = RecyclerViewBindingAdapter(cellConstructor, measureConverter)
        view.adapter = adapter

        return viewModelUpdates.subscribe({ viewModel ->

            for(decIndex in 0 until view.itemDecorationCount)
                view.removeItemDecorationAt(decIndex)

            val spacingDecoration = SpacingDecoration(
                viewModel.horSpacing,
                viewModel.verSpacing,
                viewModel.horMargins,
                measureConverter
            )

            view.addItemDecoration(spacingDecoration)

            adapter.submitList(viewModel.cells)

        }, { error ->

            print(error)
        })
    }
}

open class RecyclerViewBindingAdapter<ViewModel, CellType: RecyclerCell<ViewModel>>(
    private val constructor: (Context) -> CellType,
    private val measureConverter: MeasureConverter
): ListAdapter<CellData<ViewModel>, RecyclerViewBindingAdapter<ViewModel, CellType>.CellHolder>(ItemCallBack()) {

    inner class CellHolder(val cell: RecyclerCell<ViewModel>): RecyclerView.ViewHolder(cell.view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellHolder {

        val view = constructor(parent.context)
        return CellHolder(view)
    }

    override fun onBindViewHolder(holder: CellHolder, position: Int) {

        val cellData = getItem(position)

        val cell = holder.cell
        val viewModel = cellData.viewModel
        cell.viewModel = viewModel

        val view = cell.view

        view.setOnClickListener({ cellData.tapCommand?.invoke() })

        val layoutParams = view.getLayoutParams() as? RecyclerView.LayoutParams ?: RecyclerView.LayoutParams(0, 0)
        layoutParams.width = measureConverter.convertToPixels(cellData.size.width)
        layoutParams.height = measureConverter.convertToPixels(cellData.size.height)
        view.setLayoutParams(layoutParams)
    }

    private class ItemCallBack<ViewModel>: DiffUtil.ItemCallback<CellData<ViewModel>>() {

        override fun areItemsTheSame(
            oldItem: CellData<ViewModel>,
            newItem: CellData<ViewModel>
        ): Boolean {

            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: CellData<ViewModel>,
            newItem: CellData<ViewModel>
        ): Boolean {

            return true // Don't need to support updating cell contents yet
        }
    }
}

class SpacingDecoration(
    private val horSpacing: Int,
    private val verSpacing: Int,
    private val horMargins: Int,
    private val measureConverter: MeasureConverter
): RecyclerView.ItemDecoration() {

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

        val lastColumn = parent.childCount / spanCount - 1;

        val leftSpace = if(column > 0) horSpacing else horMargins;
        outRect.left = measureConverter.convertToPixels(leftSpace)

        val rightSpace = if(column < lastColumn) 0 else horMargins;
        outRect.right = measureConverter.convertToPixels(rightSpace)

        val topSpace = if(row > 0) verSpacing else 0;
        outRect.top = measureConverter.convertToPixels(topSpace)
    }
}