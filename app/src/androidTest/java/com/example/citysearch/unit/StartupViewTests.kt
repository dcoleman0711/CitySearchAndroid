package com.example.citysearch.unit

import android.graphics.Typeface
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.models.StartupModel
import com.example.citysearch.ui.RollingAnimationLabel
import com.example.citysearch.viewmodels.TextViewModel
import com.example.citysearch.ui.StartupViewImp
import com.example.citysearch.utilities.Font
import com.example.citysearch.viewmodels.StartupViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupViewTests {

    lateinit var steps: StartupViewSteps

    val Given: StartupViewSteps get() = steps
    val When: StartupViewSteps get() = steps
    val Then: StartupViewSteps get() = steps

    @Before
    fun setUp() {
        
        steps = StartupViewSteps()
    }

    @Test
    fun testAppTitleLabelIsBoundToViewModel() {

        val appTitleLabel = Given.appTitleLabel()
        val startupViewModel = Given.startupViewModel()

        val startupView = When.startupViewIsCreated(startupViewModel = startupViewModel)

        Then.appTitleLabelIsBoundToViewModel(appTitleLabel, startupViewModel)
    }

    @Test
    fun testTransitionIsScheduled() {

        val startupModel = Given.startupModel()

        val startupView = When.startupViewIsCreated(startupModel = startupModel)

        Then.transitionIsScheduled(startupModel)
    }
}

class StartupViewSteps {

    private val view = mock<ConstraintLayout> {  }

    private val appTitleLabel = mock<RollingAnimationLabel> {  }

    private val startupModel = mock<StartupModel> {  }
    private val appTitle = BehaviorSubject.create<TextViewModel>()

    private val viewModel = mock<StartupViewModel> {

        on { model }.thenReturn(startupModel)

        on { appTitle }.thenReturn(appTitle)
    }

    private val stubText = "stubText"
    private val stubFont = Font(Typeface.DEFAULT, 123.0)

    fun startupModel(): StartupModel {
        
        return startupModel
    }

    fun startupViewModel(): StartupViewModel {
        
        return viewModel
    }

    fun startupViewIsCreated(appTitleLabel: RollingAnimationLabel = this.appTitleLabel, startupModel: StartupModel = this.startupModel, startupViewModel: StartupViewModel = this.viewModel): StartupViewImp {

        return StartupViewImp(
            view,
            viewModel,
            appTitleLabel
        )
    }

    fun appTitleLabel(): RollingAnimationLabel {

        return appTitleLabel
    }

    fun appTitleLabelIsBoundToViewModel(appTitleLabel: RollingAnimationLabel, viewModel: StartupViewModel) {

        appTitle.onNext(
            TextViewModel(
                stubText,
                stubFont
            )
        )
        verify(appTitleLabel).start(stubText, stubFont)
    }

    fun transitionIsScheduled(startupModel: StartupModel) {

        verify(startupModel).startTransitionTimer()
    }
}