package com.example.citysearch.startup

import com.example.citysearch.data.CitySearchResults

interface StartupTransitionCommand {

    fun invoke(initialResults: CitySearchResults)
}

class StartupTransitionCommandImp: StartupTransitionCommand {

    override fun invoke(initialResults: CitySearchResults) {
        TODO("Not yet implemented")
    }


}