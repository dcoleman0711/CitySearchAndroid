package com.example.citysearch.reactive

import com.example.citysearch.utilities.Size

// Provides a command to be invoked when a cell is tapped
interface CellTapCommand {

    fun invoke()
}

// Data class for state of a RecyclerView cell.  Contains the cell's view-model, its size, and its tap command
data class CellData<ViewModel>(val viewModel: ViewModel, val size: Size, val tapCommand: CellTapCommand?) {

    override fun hashCode(): Int {

        return viewModel.hashCode() + size.hashCode()
    }

    override fun equals(other: Any?): Boolean {

        val otherCellData = other as? CellData<*>
        if(otherCellData == null)
            return false;

        return viewModel == otherCellData.viewModel &&
                size == otherCellData.size
    }
}

// Data class for the presentation state of a RecyclerView.  Contains the list of displayed cells, and spacing/margin values
data class RecyclerViewModel<ViewModel>(val cells: List<CellData<ViewModel>>, val horSpacing: Int, val verSpacing: Int, val horMargins: Int = 0)