package com.example.citysearch.models

import com.example.citysearch.entities.CitySearchResult
import com.example.citysearch.commands.OpenDetailsCommand
import com.example.citysearch.factories.OpenDetailsCommandFactory
import io.reactivex.Observable

/**
 * Model for CitySearchResult MVVM.
 *
 * Exposes the properties of the search result for display, and categorizes the population into one of 4 classes
 */
interface CitySearchResultModel {

    val title: Observable<String>
    val populationClass: Observable<PopulationClass>

    val openDetailsCommand: OpenDetailsCommand
}

class CitySearchResultModelImp(
    searchResult: CitySearchResult,
    openDetailsCommandFactory: OpenDetailsCommandFactory
): CitySearchResultModel {

    override val title = Observable.just(searchResult.nameAndState)
    override val populationClass = Observable.just(
        populationClass(
            searchResult.population
        )
    )

    override val openDetailsCommand = openDetailsCommandFactory.openDetailsCommand(searchResult)

    companion object {

        private fun populationClass(population: Int): PopulationClass {

            val allPopulations = arrayOf(
                PopulationClassSmall(),
                PopulationClassMedium(),
                PopulationClassLarge(),
                PopulationClassVeryLarge()
            )

            return allPopulations.firstOrNull { populationClass -> populationClass.range.contains(population) } ?: PopulationClassSmall()
        }
    }
}

// PopulationClass is basically a beefed up enum.  There are four "cases",
// each of which is implemented as a concrete subtype.
// The Visitor pattern is used to extend the polymorphic structure
// (i.e. provide additional functions that vary with the 4 subclasses)
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

    override fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T {

        return visitor.visitSmall(this)
    }

    override fun equals(other: Any?): Boolean {

        return other is PopulationClassSmall
    }

    override fun hashCode(): Int {

        return this::class.hashCode()
    }
}

class PopulationClassMedium: PopulationClass {

    override val range: IntRange = IntRange(10000, 99999)

    override fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T {

        return visitor.visitMedium(this)
    }

    override fun equals(other: Any?): Boolean {

        return other is PopulationClassMedium
    }

    override fun hashCode(): Int {

        return this::class.hashCode()
    }
}

class PopulationClassLarge: PopulationClass {

    override val range: IntRange = IntRange(100000, 999999)

    override fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T {

        return visitor.visitLarge(this)
    }

    override fun equals(other: Any?): Boolean {

        return other is PopulationClassLarge
    }

    override fun hashCode(): Int {

        return this::class.hashCode()
    }
}

class PopulationClassVeryLarge: PopulationClass {

    override val range: IntRange = IntRange(1000000, Int.MAX_VALUE)

    override fun<T, V: PopulationClassVisitor<T>> accept(visitor: V): T {

        return visitor.visitVeryLarge(this)
    }

    override fun equals(other: Any?): Boolean {

        return other is PopulationClassVeryLarge
    }

    override fun hashCode(): Int {

        return this::class.hashCode()
    }
}