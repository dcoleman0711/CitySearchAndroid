package com.example.citysearch.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import com.example.citysearch.models.*
import com.example.citysearch.commands.OpenDetailsCommand
import com.example.citysearch.utilities.Font
import com.example.citysearch.utilities.ImageLoader
import io.reactivex.Observable
import java.util.*

/**
 * ViewModel for CitySearchResult MVVM.  Defines the font and maps population classes to icons
 */
interface CitySearchResultViewModel {

    val title: Observable<TextViewModel>
    val iconImage: Observable<Optional<Bitmap>>

    val openDetailsCommand: OpenDetailsCommand
}

class CitySearchResultViewModelImp(
    context: Context,
    private val model: CitySearchResultModel
): CitySearchResultViewModel {

    override val title: Observable<TextViewModel>
    override val iconImage: Observable<Optional<Bitmap>>

    override val openDetailsCommand: OpenDetailsCommand

    init {

        title = model.title.map { title ->
            TextViewModel(
                title,
                Font(Typeface.DEFAULT, 12.0)
            )
        }
        iconImage = model.populationClass.map { popClass -> Optional.of(popClass.accept(
            ImageSelector(
                context
            )
        )) }

        openDetailsCommand = model.openDetailsCommand
    }

    private class ImageSelector(private val context: Context):
        PopulationClassVisitor<Bitmap> {

        override fun visitSmall(popClass: PopulationClassSmall): Bitmap { return ImageLoader.loadImage(context, "City1.png") }
        override fun visitMedium(popClass: PopulationClassMedium): Bitmap { return ImageLoader.loadImage(context, "City2.png") }
        override fun visitLarge(popClass: PopulationClassLarge): Bitmap { return ImageLoader.loadImage(context, "City3.png") }
        override fun visitVeryLarge(popClass: PopulationClassVeryLarge): Bitmap { return ImageLoader.loadImage(context, "City4.png") }
    }
}