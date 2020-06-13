package com.example.citysearch.search

import com.example.citysearch.data.CitySearchResult

interface OpenDetailsCommandFactory {

    fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand
}

class OpenDetailsCommandFactoryImp(private val searchRoot: SearchRoot): OpenDetailsCommandFactory {

    override fun openDetailsCommand(searchResult: CitySearchResult): OpenDetailsCommand {

        return OpenDetailsCommandImp(searchRoot, searchResult)
    }
}