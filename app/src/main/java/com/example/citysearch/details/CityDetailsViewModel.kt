package com.example.citysearch.details

import android.graphics.Typeface
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.reactive.TextViewModel
import com.example.citysearch.utilities.Font
import io.reactivex.Observable
import java.text.DecimalFormat

interface CityDetailsViewModel {

    val title: Observable<TextViewModel>

    val populationTitle: Observable<TextViewModel>
    val population: Observable<TextViewModel>

    val showLoader: Observable<Boolean>
}

class CityDetailsViewModelImp(private val model: CityDetailsModel): CityDetailsViewModel {

    constructor(searchResult: CitySearchResult) : this(CityDetailsModelImp(searchResult))

    override val title: Observable<TextViewModel>

    override val populationTitle: Observable<TextViewModel>
    override val population: Observable<TextViewModel>

    override val showLoader: Observable<Boolean>

    private val titleFont = Font(Typeface.DEFAULT, 36.0)
    private val populationFont = Font(Typeface.DEFAULT, 36.0)
    private val populationTitleText = "Population: "

    init {

        title = model.title.map { title -> TextViewModel(title, titleFont) }

        populationTitle = Observable.just(TextViewModel(populationTitleText, populationFont))
        population = model.population.map { population -> TextViewModel(String.format("%,d", population), populationFont) }

        showLoader = model.loading
    }
}