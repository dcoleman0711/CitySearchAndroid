package com.example.citysearch.unit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.viewmodels.TextViewModel
import com.example.citysearch.reactive.ViewBinderImp
import com.example.citysearch.utilities.Font
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ViewBinderTests {

    lateinit var steps: ViewBinderSteps

    val Given: ViewBinderSteps get() = steps
    val When: ViewBinderSteps get() = steps
    val Then: ViewBinderSteps get() = steps

    @Before
    fun setUp() {
        
        steps = ViewBinderSteps()
    }

    @Test
    fun testBindTextViewText() {

        val textView = Given.textView()
        val binder = Given.binder()
        val textViewUpdate = Given.bindTextViewData(binder, textView)
        val text = Given.text()

        When.updateText(text)

        Then.textViewTextEquals(textView, text)
    }

    @Test
    fun testBindTextViewFont() {

        val textView = Given.textView()
        val binder = Given.binder()
        val textViewUpdate = Given.bindTextViewData(binder, textView)
        val font = Given.font()

        When.updateFont(font)

        Then.textViewFontEquals(textView, font)
    }

    @Test
    fun testBindImageViewImage() {

        val imageView = Given.imageView()
        val binder = Given.binder()
        val imageUpdate = Given.bindImageViewImage(binder, imageView)
        val image = Given.image()

        When.updateImage(image)

        Then.imageViewImageEquals(imageView, image)
    }
}

class ViewBinderSteps {

    private val textView = mock<TextView> {  }
    private val imageView = mock<ImageView> {  }

    private val textUpdates = BehaviorSubject.create<TextViewModel>()
    private val imageUpdates = BehaviorSubject.create<Optional<Bitmap>>()

    fun textView(): TextView {

        return textView
    }

    fun imageView(): ImageView {

        return imageView
    }

    fun binder(): ViewBinderImp {

        return ViewBinderImp()
    }

    fun bindTextViewData(binder: ViewBinderImp, textView: TextView): Disposable {

        return binder.bindTextView(textView, textUpdates)
    }

    fun bindImageViewImage(binder: ViewBinderImp, imageView: ImageView): Disposable {

        return binder.bindImageView(imageView, imageUpdates)
    }

    fun text(): String {

        return "textText"
    }

    fun font(): Font {

        return Font(Typeface.DEFAULT, 43.4)
    }

    fun image(): Bitmap {

        val fileStream = javaClass.classLoader!!.getResourceAsStream("assets/TestImage.jpg")
        return BitmapFactory.decodeStream(fileStream)
    }

    fun updateText(text: String) {

        textUpdates.onNext(
            TextViewModel(
                text,
                Font(Typeface.DEFAULT, 12.0)
            )
        )
    }

    fun updateFont(font: Font) {

        textUpdates.onNext(
            TextViewModel(
                "",
                font
            )
        )
    }

    fun updateImage(image: Bitmap) {

        imageUpdates.onNext(Optional.of(image))
    }

    fun textViewTextEquals(textView: TextView, expectedText: String) {

        verify(textView).text = expectedText
    }

    fun textViewFontEquals(textView: TextView, expectedFont: Font) {

        verify(textView).typeface = expectedFont.typeface
        verify(textView).textSize = expectedFont.size.toFloat()
    }

    fun imageViewImageEquals(imageView: ImageView, expectedImage: Bitmap) {

        verify(imageView).setImageBitmap(expectedImage)
    }
}