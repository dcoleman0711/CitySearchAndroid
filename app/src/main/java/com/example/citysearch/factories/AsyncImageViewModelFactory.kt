package com.example.citysearch.factories

import com.example.citysearch.models.AsyncImageModel
import com.example.citysearch.viewmodels.AsyncImageViewModel
import com.example.citysearch.viewmodels.AsyncImageViewModelImp

/**
 * Factory for creating AsyncImageViewModels.
 *
 * Useful for mocking view model creation during tests
 */
interface AsyncImageViewModelFactory {

    fun viewModel(model: AsyncImageModel): AsyncImageViewModel
}

class AsyncImageViewModelFactoryImp:
    AsyncImageViewModelFactory {

    override fun viewModel(model: AsyncImageModel): AsyncImageViewModel {

        return AsyncImageViewModelImp(model)
    }
}