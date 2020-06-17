package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.reactive.TextViewModel
import com.example.citysearch.startup.StartupModel
import com.example.citysearch.startup.StartupViewModelImp
import com.example.citysearch.utilities.Font
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupViewModelTests {

    lateinit var steps: StartupViewModelSteps

    val Given: StartupViewModelSteps get() = steps
    val When: StartupViewModelSteps get() = steps
    val Then: StartupViewModelSteps get() = steps

    @Before
    fun setUp() {

        steps = StartupViewModelSteps()
    }

    @Test
    fun testObserveAppTitleText() {

        val appTitle = Given.appTitle()
        val model = Given.model(appTitle)
        val viewModel = Given.viewModel(model)

        When.observeAppTitleText(viewModel)

        Then.appTitleTextIsUpdated(appTitle)
    }

    @Test
    fun testObserveAppTitleFont() {

        val font = Given.appTitleFont()
        val model = Given.model()
        val viewModel = Given.viewModel(model)

        When.observeAppTitleText(viewModel)

        Then.appTitleTextFontIsUpdated(font)
    }
}

class StartupViewModelSteps {

    val modelAppTitle = BehaviorSubject.create<String>()

    private val model = mock<StartupModel> {

        on { appTitle }.thenReturn(modelAppTitle)
    }

    private var observedAppTitle: TextViewModel? = null

    fun appTitle(): String {

        return "Test Title"
    }

    fun appTitleFont(): Font {

        return StartupScreenTestConstants.appTitleFont
    }

    fun viewModel(model: StartupModel = this.model): StartupViewModelImp {

        return StartupViewModelImp(model)
    }

    fun model(appTitle: String = ""): StartupModel {

        modelAppTitle.onNext(appTitle)

        return model
    }

    fun observeAppTitleText(viewModel: StartupViewModelImp) {

        viewModel.appTitle.subscribe { appTitle ->

            this.observedAppTitle = appTitle
        }
    }

    fun appTitleTextIsUpdated(expectedText: String) {

        Assert.assertEquals("App title text was not updated correctly", expectedText, observedAppTitle?.text)
    }

    fun appTitleTextFontIsUpdated(expectedFont: Font) {

        Assert.assertEquals("App title font was not updated correctly", expectedFont, observedAppTitle?.font)
    }
}