package com.example.citysearch.unit

import android.content.Context
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.factories.CitySearchResultViewModelFactoryImp
import com.example.citysearch.models.*
import com.example.citysearch.viewmodels.TextViewModel
import com.example.citysearch.commands.OpenDetailsCommand
import com.example.citysearch.utilities.ImageLoader
import com.example.citysearch.viewmodels.CitySearchResultViewModelImp
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class CitySearchResultViewModelTests {

    lateinit var steps: CitySearchResultViewModelSteps

    val Given: CitySearchResultViewModelSteps get() = steps
    val When: CitySearchResultViewModelSteps get() = steps
    val Then: CitySearchResultViewModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = CitySearchResultViewModelSteps(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testTitle() {

        val titleText = Given.titleText()
        val model = Given.model(titleText = titleText)

        val viewModel = When.viewModelIsCreated(model)

        Then.viewModelTextIs(viewModel, titleText)
    }

    @Test
    fun testIcon() {

        val populationClass = Given.populationClass()
        val model = Given.model(populationClass = populationClass)
        val icon = Given.icon(populationClass)
        val viewModel = Given.viewModelIsCreated(model)

        val imageObserver = When.observerObservesIcon(viewModel)

        Then.observerReceivedValue(icon)
    }

    @Test
    fun testTapCommand() {

        val tapCommand = Given.tapCommand()
        val model = Given.model(tapCommand = tapCommand)

        val viewModel = When.viewModelIsCreated(model)

        Then.viewModelTapCommandIs(viewModel, tapCommand)
    }
}

class CitySearchResultViewModelSteps(private val context: Context) {

    private var observedImage: Optional<Bitmap>? = null

    private val openDetailsCommand = mock<OpenDetailsCommand> {  }

    private val modelTitle = BehaviorSubject.create<String>()
    private val modelPopClass = BehaviorSubject.create<PopulationClass>()

    private val model = mock<CitySearchResultModel> {

        on { title }.thenReturn(modelTitle)
        on { populationClass }.thenReturn(modelPopClass)

        on { openDetailsCommand }.thenReturn(openDetailsCommand)
    }

    fun titleText(): String {

        return "Test Title"
    }

    fun populationClass(): PopulationClass {

        return PopulationClassMedium()
    }

    fun tapCommand(): OpenDetailsCommand {

        return openDetailsCommand
    }

    fun observerObservesIcon(viewModel: CitySearchResultViewModelImp): Disposable {

        return viewModel.iconImage.subscribe { image ->

            observedImage = image
        }
    }

    fun model(titleText: String = "", populationClass: PopulationClass = PopulationClassSmall(), tapCommand: OpenDetailsCommand = this.openDetailsCommand): CitySearchResultModel {

        modelTitle.onNext(titleText)
        modelPopClass.onNext(populationClass)

        whenever(model.openDetailsCommand).thenReturn(tapCommand)

        return model
    }

    fun icon(populationClass: PopulationClass): Bitmap {

        class ImageSelector:
            PopulationClassVisitor<Bitmap> {

            override fun visitSmall(popClass: PopulationClassSmall): Bitmap { return ImageLoader.loadImage(context,"City1.png") }
            override fun visitMedium(popClass: PopulationClassMedium): Bitmap { return ImageLoader.loadImage(context,"City2.png") }
            override fun visitLarge(popClass: PopulationClassLarge): Bitmap { return ImageLoader.loadImage(context,"City3.png") }
            override fun visitVeryLarge(popClass: PopulationClassVeryLarge): Bitmap { return ImageLoader.loadImage(context,"City4.png") }
        }

        return populationClass.accept(ImageSelector())
    }

    fun viewModelIsCreated(model: CitySearchResultModel): CitySearchResultViewModelImp {

        return CitySearchResultViewModelFactoryImp()
            .resultViewModel(context, model) as CitySearchResultViewModelImp
    }

    fun viewModelTextIs(viewModel: CitySearchResultViewModelImp, expectedText: String) {

        var observedTitle: TextViewModel? = null
        viewModel.title.subscribe { title -> observedTitle = title }

        Assert.assertEquals( "Title text is not correct", expectedText, observedTitle?.text)
    }

    fun viewModelTapCommandIs(viewModel: CitySearchResultViewModelImp, expectedCommand: OpenDetailsCommand) {

        Assert.assertTrue("Tap command is not correct", viewModel.openDetailsCommand === expectedCommand)
    }

    fun observerReceivedValue(icon: Bitmap) {

        Assert.assertTrue("Icon Observer did not receive correct image", icon.sameAs(observedImage?.get()))
    }
}