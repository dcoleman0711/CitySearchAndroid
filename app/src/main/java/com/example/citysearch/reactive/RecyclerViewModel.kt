package com.example.citysearch.reactive

import com.example.citysearch.utilities.Size

interface CellTapCommand {

    fun invoke()
}

data class CellData<ViewModel>(val viewModel: ViewModel, val size: Size, val tapCommand: CellTapCommand?) {

    override fun hashCode(): Int {

        return viewModel.hashCode() + size.hashCode()
    }
}

data class RecyclerViewModel<ViewModel>(val cells: List<CellData<ViewModel>>, val horSpacing: Int, val verSpacing: Int)