package com.example.citysearch.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.util.Size
import androidx.lifecycle.ViewModel
import com.example.citysearch.entities.GeoPoint
import com.example.citysearch.models.MapModel
import com.example.citysearch.utilities.ImageLoader
import io.reactivex.Observable
import java.util.*

/**
 * Model for Map MVVM.
 *
 * Converts the model's geo-location into a percentage-based visual location, and the pin's size.
 * Also defines the images for the Earth background and pin
 */
interface MapViewModel {

    companion object {

        const val resolution = 1.0e6f
    }

    val backgroundImage: Observable<Optional<Bitmap>>
    val markerImage: Observable<Optional<Bitmap>>
    val markerFrame: Observable<Rect>
}

class MapViewModelImp(
    context: Context,
    private val model: MapModel
): MapViewModel, ViewModel() {

    override val backgroundImage: Observable<Optional<Bitmap>>
    override val markerImage: Observable<Optional<Bitmap>>
    override val markerFrame: Observable<Rect>

    private val imgBackground = ImageLoader.loadImage(context, "MapBackground.jpg")
    private val imgMarker = ImageLoader.loadImage(context, "MapMarker.png")

    private val markerSize = Size(16, 16)

    init {

        backgroundImage = Observable.just(Optional.of(imgBackground))
        markerImage = Observable.just(Optional.of(imgMarker))

        markerFrame = model.geoCoordinates.map { geoCoordinates -> markerFrame(geoCoordinates) }
    }

    private fun markerFrame(geoCoordinates: GeoPoint): Rect {

        val xPercent = ((geoCoordinates.longitude / 360.0) + 0.5).rem(1.0)
        val yPercent = 1.0 - (geoCoordinates.latitude + 90.0) / 180.0

        val position = Point(
            (xPercent * MapViewModel.resolution).toInt(),
            (yPercent * MapViewModel.resolution).toInt()
        )

        return Rect(
            position.x,
            position.y,
            position.x + markerSize.width,
            position.y + markerSize.height
        )
    }
}