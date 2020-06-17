package com.example.citysearch.unit

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.animations.RollingAnimationLabel
import com.example.citysearch.reactive.TextViewModel
import com.example.citysearch.startup.*
import com.example.citysearch.utilities.ConstraintSetFactory
import com.example.citysearch.utilities.Font
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
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
    fun testAppTitleLabelIsOnStartupView() {

        val appTitleLabel = Given.appTitleLabel()
        val startupViewModel = Given.startupViewModel()

        val startupView = When.startupViewIsCreated(startupViewModel = startupViewModel, appTitleLabel =  appTitleLabel)

        Then.appTitleLabelIsOnStartupView(appTitleLabel, startupView)
    }

    @Test
    fun testAppTitleLabelCenter() {

        val appTitleLabel = Given.appTitleLabel()
        val expectedConstraints = Given.constraintsForCenter(appTitleLabel)

        val startupView = When.startupViewIsCreated(appTitleLabel)

        Then.startupViewHasConstraints(startupView, expectedConstraints)
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

    private val context = mock<Context> {

//        on { resources }.thenAnswer {
//
//            val resources = mock<Resources> {
//
//                on { displayMetrics }.thenAnswer {
//
//                    val displayMetrics = mock<DisplayMetrics> {  }
//
//                    displayMetrics
//                }
//
//                on { configuration }.thenAnswer {
//
//                    val configuration = Configuration()
//                }
//            }
//
//            resources
//        }
    }

    private val view = mock<ConstraintLayout> {  }

    private val appTitleLabel = mock<RollingAnimationLabel> {  }

    private val constraints = mock<ConstraintSet> {  }

    private val constraintSetFactory = mock<ConstraintSetFactory> {

        on { constraintSet() }.thenReturn(constraints)
    }

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

    fun constraintsForCenter(appTitleLabel: RollingAnimationLabel): () -> Unit {

        return {

            verify(constraints).centerHorizontally(appTitleLabel.id, ConstraintSet.PARENT_ID)
            verify(constraints).centerVertically(appTitleLabel.id, ConstraintSet.PARENT_ID)
            verify(constraints).constrainWidth(appTitleLabel.id, ConstraintSet.WRAP_CONTENT)
            verify(constraints).constrainHeight(appTitleLabel.id, ConstraintSet.WRAP_CONTENT)
        }
    }

    fun startupViewIsCreated(appTitleLabel: RollingAnimationLabel = this.appTitleLabel, startupModel: StartupModel = this.startupModel, startupViewModel: StartupViewModel = this.viewModel): StartupViewImp {

        return StartupViewImp(context, viewModel, view, appTitleLabel, constraintSetFactory)
    }

    fun appTitleLabel(): RollingAnimationLabel {

        return appTitleLabel
    }

    fun appTitleLabelIsOnStartupView(appTitleLabel: RollingAnimationLabel, startupView: StartupView) {

        verify(view).addView(appTitleLabel)
    }

    fun startupViewHasConstraints(startupView: StartupView, expectedConstraints: () -> Unit) {

        expectedConstraints()
    }

    fun appTitleLabelIsBoundToViewModel(appTitleLabel: RollingAnimationLabel, viewModel: StartupViewModel) {

        appTitle.onNext(TextViewModel(stubText, stubFont))
        verify(appTitleLabel).start(stubText, stubFont)
    }

    fun transitionIsScheduled(startupModel: StartupModel) {

        verify(startupModel).startTransitionTimer()
    }
}