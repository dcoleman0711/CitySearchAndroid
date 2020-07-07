package com.example.citysearch.reactive

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
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

class RecyclerViewBinderImp<ViewModel, CellType: RecyclerCell<ViewModel>>(private val cellConstructor: (Context) -> CellType, private val measureConverter: MeasureConverter): RecyclerViewBinder<ViewModel, CellType> {

    constructor(context: Context, constructor: (Context) -> CellType) : this(constructor, MeasureConverterImp(context))

    constructor(context: Context, layout: Int, cellConstructor: (View) -> CellType) : this(context, { context ->

        val view = LayoutInflater.from(context).inflate(layout, null)
        cellConstructor(view)
    })

    override fun bindCells(view: RecyclerView, viewModelUpdates: Observable<RecyclerViewModel<ViewModel>>): Disposable {

        val adapter = RecyclerViewBindingAdapter(cellConstructor, measureConverter)
        view.adapter = adapter

        return viewModelUpdates.subscribe({ viewModel ->

            for(decIndex in 0 until view.itemDecorationCount)
                view.removeItemDecorationAt(decIndex)

            val spacingDecoration = SpacingDecoration<ViewModel>(viewModel.horSpacing, viewModel.verSpacing, measureConverter)
            view.addItemDecoration(spacingDecoration)

            adapter.cellData = viewModel.cells
            adapter.notifyDataSetChanged()

        }, { error ->

            print(error)
        })
    }
}

open class RecyclerViewBindingAdapter<ViewModel, CellType: RecyclerCell<ViewModel>>(private val constructor: (Context) -> CellType, private val measureConverter: MeasureConverter): RecyclerView.Adapter<RecyclerViewBindingAdapter<ViewModel, CellType>.CellHolder>() {

    inner class CellHolder(val cell: RecyclerCell<ViewModel>): RecyclerView.ViewHolder(cell.view)

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

        val view = cell.view

        view.setOnClickListener({ cellData.tapCommand?.invoke() })

        val layoutParams = view.getLayoutParams() as? RecyclerView.LayoutParams ?: RecyclerView.LayoutParams(0, 0)
        layoutParams.width = measureConverter.convertToPixels(cellData.size.width)
        layoutParams.height = measureConverter.convertToPixels(cellData.size.height)
        view.setLayoutParams(layoutParams)
    }

    var cellData: List<CellData<ViewModel>> = arrayListOf()
}

class SpacingDecoration<ViewModel>(private val horSpacing: Int, private val verSpacing: Int, private val measureConverter: MeasureConverter): RecyclerView.ItemDecoration() {

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