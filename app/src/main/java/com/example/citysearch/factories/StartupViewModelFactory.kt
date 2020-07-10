package com.example.citysearch.factories

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citysearch.services.CitySearchService
import com.example.citysearch.services.CitySearchServiceImp
import com.example.citysearch.models.StartupModelImp
import com.example.citysearch.commands.StartupTransitionCommand
import com.example.citysearch.commands.StartupTransitionCommandImp
import com.example.citysearch.viewmodels.StartupViewModelImp

/**
 * JetPack factory for StartupViewModel, to support passing in the context and fragment manager
 */
class StartupViewModelFactory(private val fragmentManager: FragmentManager): ViewModelProvider.Factory {

    var transitionCommand: StartupTransitionCommand? = null
    var searchService: CitySearchService =
        CitySearchServiceImp()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        val transitionCommand = this.transitionCommand ?: StartupTransitionCommandImp(
            fragmentManager
        )
        val model = StartupModelImp(
            transitionCommand,
            searchService
        )
        return StartupViewModelImp(model) as T
    }
}