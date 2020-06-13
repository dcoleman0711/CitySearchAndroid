package com.example.citysearch.search.searchresults.citysearchresultcell

import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.search.OpenDetailsCommand
import com.example.citysearch.search.OpenDetailsCommandFactory
import io.reactivex.Observable

interface CitySearchResultModel {

    val title: Observable<String>
    val populationClass: Observable<PopulationClass>

    val openDetailsCommand: OpenDetailsCommand
}

class CitySearchResultModelImp(searchResult: CitySearchResult, openDetailsCommandFactory: OpenDetailsCommandFactory): CitySearchResultModel {

    override val title: Observable<String>
    override val populationClass: Observable<PopulationClass>

    override val openDetailsCommand: OpenDetailsCommand

    init {

        title = Observable.just(searchResult.name)
        populationClass = Observable.just(populationClass(searchResult.population))

        openDetailsCommand = openDetailsCommandFactory.openDetailsCommand(searchResult)
    }

    companion object {

        private fun populationClass(population: Int): PopulationClass {

            val allPopulations = arrayOf(PopulationClassSmall(), PopulationClassMedium(), PopulationClassLarge(), PopulationClassVeryLarge())

            return allPopulations.firstOrNull { populationClass -> populationClass.range.contains(population) } ?: PopulationClassSmall()
        }
    }
}

interface PopulationClass {

    val range: IntRange

    fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T
}

interface PopulationClassVisitor<T> {

    fun visitSmall(popClass: PopulationClassSmall): T
    fun visitMedium(popClass: PopulationClassMedium): T
    fun visitLarge(popClass: PopulationClassLarge): T
    fun visitVeryLarge(popClass: PopulationClassVeryLarge): T
}

class PopulationClassSmall: PopulationClass {

    override val range: IntRange = IntRange(0, 9999)

    override fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T { return visitor.visitSmall(this) }
}

class PopulationClassMedium: PopulationClass {

    override val range: IntRange = IntRange(10000, 99999)

    override fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T { return visitor.visitMedium(this) }
}

class PopulationClassLarge: PopulationClass {

    override val range: IntRange = IntRange(100000, 999999)

    override fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T { return visitor.visitLarge(this) }
}

class PopulationClassVeryLarge: PopulationClass {

    override val range: IntRange = IntRange(1000000, Int.MAX_VALUE)

    override fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T { return visitor.visitVeryLarge(this) }
}