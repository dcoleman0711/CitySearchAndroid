package com.example.citysearch.startup

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.data.CitySearchService
import com.example.citysearch.data.CitySearchServiceImp

/**
 * JetPack factory for StartupViewModel, to support passing in the context and fragment manager
 */
class StartupViewModelFactory(private val fragmentManager: FragmentManager): ViewModelProvider.Factory {

    var transitionCommand: StartupTransitionCommand? = null
    var searchService: CitySearchService = CitySearchServiceImp()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        val transitionCommand = this.transitionCommand ?: StartupTransitionCommandImp(fragmentManager)
        val model = StartupModelImp(transitionCommand, searchService)
        return StartupViewModelImp(model) as T
    }
}