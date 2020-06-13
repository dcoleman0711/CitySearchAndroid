package com.example.citysearch.search

import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.reactive.CellTapCommand

interface OpenDetailsCommand: CellTapCommand

class OpenDetailsCommandImp(searchResult: CitySearchResult): OpenDetailsCommand {

    override fun invoke() {

    }
}