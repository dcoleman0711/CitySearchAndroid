package com.example.citysearch.reactive

import com.example.citysearch.utilities.Size

interface CellTapCommand {

    fun invoke()
}

data class CellData<ViewModel>(val viewModel: ViewModel, val size: Size, val tapCommand: CellTapCommand?)

data class RecyclerViewModel<ViewModel>(val cells: Array<CellData<ViewModel>>, val horSpacing: Int, val verSpacing: Int)