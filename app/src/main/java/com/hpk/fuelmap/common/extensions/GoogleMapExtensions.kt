package com.hpk.fuelmap.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RawRes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.SphericalUtil


@SuppressLint("MissingPermission")
fun GoogleMap.setMapStyle(
    context: Context,
    @RawRes rawResourceStyleId: Int,
    isWithMyLocation: Boolean = false
) {
    setMapStyle(MapStyleOptions.loadRawResourceStyle(context, rawResourceStyleId))
    if (isWithMyLocation) {
        isMyLocationEnabled = true
        uiSettings.isMyLocationButtonEnabled = false
    }
    with(uiSettings) {
        isCompassEnabled = false
        isMapToolbarEnabled = false
        isZoomControlsEnabled = false
    }
}

@SuppressLint("MissingPermission")
fun GoogleMap.setDefaultMapStyle(
    isWithMyLocation: Boolean = false
) {
    if (isWithMyLocation) {
        isMyLocationEnabled = true
        uiSettings.isMyLocationButtonEnabled = true
    }
    with(uiSettings) {
        isCompassEnabled = false
        isMapToolbarEnabled = false
        isZoomControlsEnabled = false
    }
}

@SuppressLint("MissingPermission")
fun GoogleMap.tryToSetMyLocationEnabled(isWithMyLocation: Boolean) {
    isMyLocationEnabled = isWithMyLocation
    uiSettings.isMyLocationButtonEnabled = false
}

fun GoogleMap.moveToPolylineBounds(polyline: Polyline, padding: Int = 40.dp) {
    try {
        val builder = LatLngBounds.Builder()
        for (i in 0 until polyline.points.size) {
            builder.include(polyline.points[i])
        }

        val bounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        animateCamera(cu)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun GoogleMap.disableMapGestures() {
    uiSettings.isScrollGesturesEnabled = false
    uiSettings.isZoomGesturesEnabled = false
    uiSettings.isRotateGesturesEnabled = false
}

fun GoogleMap.enableMapGestures() {
    uiSettings.isScrollGesturesEnabled = true
    uiSettings.isZoomGesturesEnabled = true
    uiSettings.isRotateGesturesEnabled = true
}

fun LatLng.calculateBearing(currentLocation: LatLng): Float {
    return SphericalUtil.computeHeading(this, currentLocation).toFloat()
}

fun LatLng.calculateDistance(nextLocation: LatLng): Float {
    val first = Location("first").apply {
        latitude = this@calculateDistance.latitude
        longitude = this@calculateDistance.longitude
    }
    val second = Location("second").apply {
        latitude = nextLocation.latitude
        longitude = nextLocation.longitude
    }
    return first.distanceTo(second)
}
