package com.example.citysearch.startup

interface StartupViewModelFactory {

    fun startupViewModel(transitionCommand: StartupTransitionCommand): StartupViewModel
}

class StartupViewModelFactoryImp: StartupViewModelFactory {

    override fun startupViewModel(transitionCommand: StartupTransitionCommand): StartupViewModel {

        return StartupViewModelImp(transitionCommand)
    }
}