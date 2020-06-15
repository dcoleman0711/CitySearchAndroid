package com.example.citysearch.details.map

import android.content.Context
import android.graphics.Bitmap
import com.example.citysearch.data.GeoPoint
import com.example.citysearch.utilities.ImageLoader
import com.example.citysearch.utilities.Point
import com.example.citysearch.utilities.Rect
import com.example.citysearch.utilities.Size
import io.reactivex.Observable

interface MapViewModel {

    val backgroundImage: Observable<Bitmap>
    val markerImage: Observable<Bitmap>
    val markerFrame: Observable<Rect>
}

class MapViewModelImp(context: Context, private val model: MapModel): MapViewModel {

    override val backgroundImage: Observable<Bitmap>
    override val markerImage: Observable<Bitmap>
    override val markerFrame: Observable<Rect>

    private val imgBackground = ImageLoader.loadImage(context, "MapBackground.jpg")
    private val imgMarker = ImageLoader.loadImage(context, "MapMarker.png")

    private val markerSize = Size(16, 16)

    init {

        backgroundImage = Observable.just(imgBackground)
        markerImage = Observable.just(imgMarker)

        markerFrame = model.geoCoordinates.map { geoCoordinates -> markerFrame(geoCoordinates) }
    }

    private fun markerFrame(geoCoordinates: GeoPoint): Rect {

        val xPercent = ((geoCoordinates.longitude / 360.0) + 0.5).rem(1.0)
        val yPercent = 1.0 - (geoCoordinates.latitude + 90.0) / 180.0

        val position = Point((xPercent * 100.0).toInt(), (yPercent * 100.0).toInt())

        return Rect(position, markerSize)
    }
}