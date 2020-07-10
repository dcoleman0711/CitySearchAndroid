package com.example.citysearch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.citysearch.R
import com.example.citysearch.factories.StartupViewModelFactory
import com.example.citysearch.ui.StartupView
import com.example.citysearch.ui.StartupViewImp
import com.example.citysearch.viewmodels.StartupViewModelImp

/**
 * Fragment for Startup.
 *
 * Handles building the various components and managing the lifecycle
 */
class StartupFragment: Fragment() {

    private lateinit var startupView: StartupView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModelFactory =
            StartupViewModelFactory(
                parentFragmentManager
            )
        val viewModel: StartupViewModelImp by activityViewModels { viewModelFactory }

        val view = inflater.inflate(R.layout.startup, container, false)

        startupView = StartupViewImp(view, viewModel)
        return view
    }
}