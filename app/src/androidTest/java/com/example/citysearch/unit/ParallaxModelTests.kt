package com.example.citysearch.unit

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.models.ParallaxLayer
import com.example.citysearch.models.ParallaxModelImp
import io.reactivex.disposables.Disposable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParallaxModelTests {

    lateinit var steps: ParallaxModelSteps

    val Given: ParallaxModelSteps get() = steps
    val When: ParallaxModelSteps get() = steps
    val Then: ParallaxModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = ParallaxModelSteps()
    }

    @Test
    fun testLayers() {

        val model = Given.model()
        val parallaxLayers = Given.parallaxLayers()
        val layersObserver = Given.observeLayers(model)

        When.modelSetLayers(model, parallaxLayers)

        Then.layersAreUpdated(model, parallaxLayers)
    }
}

class ParallaxModelSteps {

    private var updatedLayers: List<ParallaxLayer>? = null

    fun model(): ParallaxModelImp {

        return ParallaxModelImp()
    }

    fun parallaxLayers(): List<ParallaxLayer> {

        return (0 until 5).map { index ->
            ParallaxLayer(
                10.0f / (index + 1).toFloat(),
                Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
            )
        }
    }

    fun observeLayers(model: ParallaxModelImp): Disposable {

        return model.layers.subscribe { layers ->

            updatedLayers = layers
        }
    }

    fun modelSetLayers(model: ParallaxModelImp, layers: List<ParallaxLayer>) {

        model.setLayers(layers)
    }

    fun layersAreUpdated(model: ParallaxModelImp, expectedLayers: List<ParallaxLayer>) {

        Assert.assertEquals("Model did not publish correct layers", expectedLayers, updatedLayers)
    }
}