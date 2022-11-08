package com.hpk.fuelmap.features.main.map

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.hpk.domain.models.common.Coordinates
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.extensions.requestAppPermission
import com.hpk.fuelmap.common.extensions.setDefaultMapStyle
import com.hpk.fuelmap.common.extensions.showPermissionRequiredDialog
import com.hpk.fuelmap.common.ui.base.BaseFragment
import com.hpk.fuelmap.features.main.MainVM
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import splitties.toast.longToast
import splitties.toast.toast


class MainMapFragment : BaseFragment(R.layout.fragment_main_map) {
    companion object {
        private const val ZOOM_LEVEL = 16.0f
    }

    private val viewModel: MainVM by sharedViewModel()
    private var googleMap: GoogleMap? = null
    private var isLocationApprove = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        observeData()
        checkLocationPermission()
    }

    override fun onResume() {
        super.onResume()
        observerCurrentLocation()
    }

    private fun observeData() {
        observerCurrentLocation()
        observeLocationError()
    }

    private fun checkLocationPermission() {
        requireContext().requestAppPermission {
            permission(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) {
                granted {
                    isLocationApprove = true
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

    private fun initMap() {
        (childFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment)?.let { supportMapFragment ->
            supportMapFragment.getMapAsync {
                with(it) {
                    googleMap = this
                    googleMap?.setDefaultMapStyle(isLocationApprove)
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
            val position = LatLng(it.latitude,
                it.longitude)
            position.let {
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, ZOOM_LEVEL))
            }
        }
    }
}