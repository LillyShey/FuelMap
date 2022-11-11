package com.hpk.fuelmap.features.main.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.hpk.fuelmap.R

class MarkerClusterRender(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MarkerClusterItem>,
) : DefaultClusterRenderer<MarkerClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(
        item: MarkerClusterItem,
        markerOptions: MarkerOptions,
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_fuel_marker))
    }

}