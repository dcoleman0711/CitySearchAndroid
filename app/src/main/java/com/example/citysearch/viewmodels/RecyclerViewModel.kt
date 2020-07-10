package com.example.citysearch.viewmodels

import android.util.Size

/**
 * Provides a command to be invoked when a cell is tapped
 */
interface CellTapCommand {

    fun invoke()
}

/**
 * Data class for state of a RecyclerView cell.
 *
 * @param ViewModel The type of the ViewModel for the recycler view's cells
 * @property viewModel The ViewModel for the cell
 * @property size The size of the cell
 * @property tapCommand The command invoked when the cell is tapped
 */
data class CellData<ViewModel>(
    val viewModel: ViewModel,
    val size: Size,
    val tapCommand: CellTapCommand?
) {

    override fun hashCode(): Int {

        return viewModel.hashCode() + size.hashCode()
    }

    override fun equals(other: Any?): Boolean {

        val otherCellData = other as? CellData<*> ?: return false

        return viewModel == otherCellData.viewModel &&
                size == otherCellData.size
    }
}

/**
 * Data class for the presentation state of a RecyclerView.
 *
 * @param ViewModel The type of the ViewModel for the recycler view's cells
 * @property cells The list of cells to display
 * @property horSpacing The horizontal spacing between adjacent columns
 * @property verSpacing The vertical spacing between adjacent rows
 * @property horMargins The horizontal spacing between the first column and the left edge, and the last column and the right edge
 */
data class RecyclerViewModel<ViewModel>(
    val cells: List<CellData<ViewModel>>,
    val horSpacing: Int,
    val verSpacing: Int,
    val horMargins: Int = 0
)