package com.example.citysearch.details.map

import android.content.Context
import android.graphics.Bitmap
import com.example.citysearch.data.GeoPoint
import com.example.citysearch.utilities.ImageLoader
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.Rect
import com.example.citysearch.utilities.Size
import io.reactivex.Observable
import java.util.*

interface MapViewModel {

    companion object {

        const val resolution = 1.0e6f
    }

    val backgroundImage: Observable<Optional<Bitmap>>
    val markerImage: Observable<Optional<Bitmap>>
    val markerFrame: Observable<Rect>
}

class MapViewModelImp(context: Context, private val model: MapModel): MapViewModel {

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

        val position = Point((xPercent * MapViewModel.resolution).toInt(), (yPercent * MapViewModel.resolution).toInt())

        return Rect(position, markerSize)
    }
}