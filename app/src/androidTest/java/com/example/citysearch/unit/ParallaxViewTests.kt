package com.example.citysearch.unit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.parallax.ParallaxViewImp
import com.example.citysearch.parallax.ParallaxViewModel
import com.example.citysearch.utilities.CollectionUtilities
import com.example.citysearch.utilities.ConstraintSetFactory
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.ViewFactory
import com.nhaarman.mockitokotlin2.*
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParallaxViewTests {

    lateinit var steps: ParallaxViewSteps

    val Given: ParallaxViewSteps get() = steps
    val When: ParallaxViewSteps get() = steps
    val Then: ParallaxViewSteps get() = steps

    @Before
    fun setUp() {

        steps = ParallaxViewSteps()
    }

    @Test
    fun testImageViewsImages() {

        val viewModel = Given.viewModel()
        val images = Given.images()
        val parallaxView = Given.parallaxViewIsCreated(viewModel = viewModel)

        When.viewModelPublishesImages(viewModel, images)

        Then.parallaxViewHasImageViewsWith(parallaxView, images)
    }

    @Test
    fun testImageViewsConstraints() {

        val viewModel = Given.viewModel()
        val images = Given.images()
        val parallaxView = Given.parallaxViewIsCreated(viewModel = viewModel)

        When.viewModelPublishesImages(viewModel, images)

        Then.parallaxViewAllImageViewsAreConstrainedToTopAndBottomWithCorrectAspectRatiosFor(parallaxView, images)
    }

    @Test
    fun testImageViewsOffsets() {

        val viewModel = Given.viewModel()
        val images = Given.images()
        val offsets = Given.offsets(images)
        val parallaxView = Given.parallaxViewIsCreated(viewModel = viewModel)
        Given.viewModelPublishesImages(viewModel, images)

        When.viewModelPublishesOffsets(viewModel, offsets)

        Then.parallaxViewImageViewsHaveOffsets(parallaxView, offsets)
    }
}

class ParallaxViewSteps {

    private val childViews = ArrayList<View>()

    private val view = mock<ConstraintLayout> {

        on { addView(any()) }.thenAnswer { invocation ->

            val view = invocation.getArgument<View>(0)
            childViews.add(view)

            null
        }

        on { childCount }.thenAnswer {

            childViews.size
        }

        on { getChildAt(any()) }.thenAnswer { invocation ->

            val index = invocation.getArgument<Int>(0)

            childViews[index]
        }
    }

    private val viewModelImages = BehaviorSubject.create<Array<Bitmap>>()
    private val viewModelOffsets = BehaviorSubject.create<Array<Point>>()

    private val viewModel = mock<ParallaxViewModel> {

        on { images }.thenReturn(viewModelImages)
        on { offsets }.thenReturn(viewModelOffsets)
    }

    private val imageMap = HashMap<View, Bitmap>()

    private val viewFactory = mock<ViewFactory> {

        on { imageView() }.thenAnswer {

            val imageView = mock<ImageView>()
            whenever(imageView.setImageBitmap(any())).thenAnswer { invocation ->

                val image = invocation.getArgument<Bitmap>(0)
                imageMap.set(imageView, image)

                null
            }

            imageView
        }
    }

    private val constraints = mock<ConstraintSet> {  }

    private val constraintSetFactory = mock<ConstraintSetFactory> {

        on { constraintSet() }.thenReturn(constraints)
    }

    fun images(): List<Bitmap> {

        val fileStream = javaClass.classLoader!!.getResourceAsStream("assets/TestImage.jpg")
        val bitmap = BitmapFactory.decodeStream(fileStream)

        return (0 until 5).map {

            bitmap
        }
    }

    fun offsets(images: List<Bitmap>): List<Point> {

        return (0 until images.size).map { index -> Point(index * 100, 0) }
    }

    fun viewModel(): ParallaxViewModel {

        return viewModel
    }

    fun parallaxViewIsCreated(viewModel: ParallaxViewModel = this.viewModel): ParallaxViewImp {

        return ParallaxViewImp(view, viewModel, viewFactory, constraintSetFactory)
    }

    fun viewModelPublishesImages(viewModel: ParallaxViewModel, images: List<Bitmap>) {

        viewModelImages.onNext(images.toTypedArray())
    }

    fun viewModelPublishesOffsets(viewModel: ParallaxViewModel, offsets: List<Point>) {

        viewModelOffsets.onNext(offsets.toTypedArray())
    }

    fun parallaxViewHasImageViewsWith(parallaxView: ParallaxViewImp, expectedImages: List<Bitmap>) {

        val images = childViews.map { view -> imageMap[view]!! }

        Assert.assertEquals("Image views do not have the correct images", expectedImages, images)
    }

    fun parallaxViewAllImageViewsAreConstrainedToTopAndBottomWithCorrectAspectRatiosFor(parallaxView: ParallaxViewImp, images: List<Bitmap>) {

        for(view in childViews) {

            val image = imageMap[view]!!

            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            verify(constraints, atLeastOnce()).connect(view.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
            verify(constraints, atLeastOnce()).setDimensionRatio(view.id, "${image.width}:${image.height}")
        }
    }

    fun parallaxViewImageViewsHaveOffsets(parallaxView: ParallaxViewImp, offsets: List<Point>) {

        val offsetsAndImageViews = CollectionUtilities.zip(offsets, childViews)

        for(offsetAndImageView in offsetsAndImageViews) {

            val offset = offsetAndImageView.first
            val view = offsetAndImageView.second

            verify(view).translationX = offset.x.toFloat()
        }
    }
}