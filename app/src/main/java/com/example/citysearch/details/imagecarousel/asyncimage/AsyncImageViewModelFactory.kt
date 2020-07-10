package com.example.citysearch.details.imagecarousel.asyncimage

/**
 * Factory for creating AsyncImageViewModels.
 *
 * Useful for mocking view model creation during tests
 */
interface AsyncImageViewModelFactory {

    fun viewModel(model: AsyncImageModel): AsyncImageViewModel
}

class AsyncImageViewModelFactoryImp: AsyncImageViewModelFactory {

    override fun viewModel(model: AsyncImageModel): AsyncImageViewModel {

        return AsyncImageViewModelImp(model)
    }
}