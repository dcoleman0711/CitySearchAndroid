package com.example.citysearch.unit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.data.GeoPoint
import com.example.citysearch.details.map.MapModelImp
import io.reactivex.disposables.Disposable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapModelTests {

    lateinit var steps: MapModelSteps

    val Given: MapModelSteps get() = steps
    val When: MapModelSteps get() = steps
    val Then: MapModelSteps get() = steps

    @Before
    fun setUp() {
        
        steps = MapModelSteps()
    }

    @Test
    fun testGeoCoordinates() {

        val searchResult = Given.searchResult()
        val geoCoordinates = Given.geoCoordinates(searchResult)
        val model = Given.modelIsCreated(searchResult)

        val geoCoordinatesObserver = When.observeModel(model)

        Then.geoCoordinatesIsUpdatedTo(geoCoordinates)
    }
}

class MapModelSteps {

    private var updatedGeoCoordinates: GeoPoint? = null

    fun searchResult(): CitySearchResult {

        return CitySearchResult("Test City", 0, GeoPoint(34.0, 45.0))
    }

    fun geoCoordinates(searchResult: CitySearchResult): GeoPoint {

        return searchResult.location
    }

    fun modelIsCreated(searchResult: CitySearchResult): MapModelImp {

        return MapModelImp(searchResult)
    }

    fun observeModel(model: MapModelImp): Disposable {

        return model.geoCoordinates.subscribe { geoCoordinates ->

            updatedGeoCoordinates = geoCoordinates
        }
    }

    fun geoCoordinatesIsUpdatedTo(expectedGeoCoordinates: GeoPoint) {

        Assert.assertEquals("Observer did not receive correct geo-coordinates", expectedGeoCoordinates, updatedGeoCoordinates)
    }
}