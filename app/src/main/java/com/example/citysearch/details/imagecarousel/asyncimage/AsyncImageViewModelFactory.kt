package com.example.citysearch.details.imagecarousel.asyncimage

interface AsyncImageViewModelFactory {

    fun viewModel(model: AsyncImageModel): AsyncImageViewModel
}

class AsyncImageViewModelFactoryImp: AsyncImageViewModelFactory {

    override fun viewModel(model: AsyncImageModel): AsyncImageViewModel {

        return AsyncImageViewModelImp(model)
    }
}