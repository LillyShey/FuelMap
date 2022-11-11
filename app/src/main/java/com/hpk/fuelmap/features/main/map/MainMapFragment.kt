package com.hpk.fuelmap.features.main.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager
import com.hpk.domain.models.common.Coordinates
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.extensions.requestAppPermission
import com.hpk.fuelmap.common.extensions.setDefaultMapStyle
import com.hpk.fuelmap.common.extensions.showPermissionRequiredDialog
import com.hpk.fuelmap.common.ui.base.BaseFragment
import com.hpk.fuelmap.features.main.MainVM
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import splitties.toast.UnreliableToastApi
import splitties.toast.longToast
import splitties.toast.toast

@OptIn(UnreliableToastApi::class)
class MainMapFragment : BaseFragment(R.layout.fragment_main_map) {
    companion object {
        private const val ZOOM_LEVEL = 16.0f
    }

    private val viewModel: MainVM by sharedViewModel()
    private var googleMap: GoogleMap? = null
    private var isLocationApprove = false
    private var clusterManager: ClusterManager<MarkerClusterItem>? = null
    private var clusterRender: MarkerClusterRender? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationPermission()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    private fun observeData() {
        observerCurrentLocation()
        observeLocationError()
        observeStations()
    }

    private fun checkLocationPermission() {
        requireContext().requestAppPermission {
            permission(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) {
                granted {
                    isLocationApprove = true
                    initMap()
                    viewModel.getCurrentLocation()
                }
                denied {
                    isLocationApprove = false
                    if (it.isPermanentlyDenied) {
                        requireContext().showPermissionRequiredDialog(getString(R.string.permission_permanently_denied_location_message))
                    } else {
                        toast(R.string.permission_required_location_message)
                    }
                }
            }
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun initMap() {
        (childFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment)?.let { supportMapFragment ->
            supportMapFragment.getMapAsync {
                with(it) {
                    this.setDefaultMapStyle(isLocationApprove)
                    this.setOnCameraIdleListener {
                        viewModel.setLatLngBounds(it.projection.visibleRegion.latLngBounds)
                        clusterManager?.cluster()
                    }
                    this.clear()
                    clusterManager = ClusterManager(requireContext(), this, MarkerManager(this))
                    clusterManager?.let { cluster ->
                        cluster.setOnClusterItemClickListener { markerItem ->
                            viewModel.getStationData(markerItem.getId())
                            true
                        }
                        clusterRender = MarkerClusterRender(requireContext(), this, cluster)
                        cluster.renderer = clusterRender
                    }
                    googleMap = this

                }
            }
        }
    }

    private fun observeStations() {
        viewModel.stations.observe(viewLifecycleOwner) { stations ->
            clusterManager?.clearItems()
            stations?.map { station ->
                station.coordinates.latitude?.let { latitude ->
                    station.coordinates.longitude?.let { longitude ->
                        val marker = MarkerClusterItem(LatLng(latitude, longitude), station.id)
                        clusterManager?.addItem(marker)
                    }
                }
            }
        }
    }


    private fun observerCurrentLocation() {
        viewModel.currentLocation.observe(viewLifecycleOwner) { coordinates ->
            coordinates?.let {
                moveCameraCurrentLocation(it)
            }
        }
    }

    private fun observeLocationError() {
        viewModel.noAnyLocationError.observe(viewLifecycleOwner) {
            longToast(R.string.all_no_location_providers)
        }
    }

    private fun moveCameraCurrentLocation(coordinates: Coordinates?) {
        coordinates?.let { it ->
            val position = it.latitude?.let { latitude ->
                it.longitude?.let { longitude ->
                    LatLng(latitude,
                        longitude)
                }
            }
            position?.let {
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, ZOOM_LEVEL))
            }
        }
    }
}