package com.example.citysearch.startup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.citysearch.R

// Fragment for Startup.  Handles building the various components and managing the lifecycle
class StartupFragment: Fragment() {

    companion object {

        lateinit var context: Context
    }

    private lateinit var startupView: StartupView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModelFactory = StartupViewModelFactory(requireContext(), parentFragmentManager)
        val viewModel: StartupViewModelImp by activityViewModels { viewModelFactory }

        val view = inflater.inflate(R.layout.startup, null)

        startupView = StartupViewImp(view, viewModel)
        return view
    }
}