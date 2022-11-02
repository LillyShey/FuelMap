package com.hpk.fuelmap.features.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.extensions.requestAppPermission
import com.hpk.fuelmap.common.ui.base.BaseActivity
import com.hpk.fuelmap.databinding.ActivityMainBinding
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.hpk.fuelmap.common.extensions.showPermissionRequiredDialog
import com.hpk.fuelmap.features.main.map.MainMapVM
import com.hpk.fuelmap.features.main.settings.MainSettingsVM
import splitties.toast.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val settingsViewModel: MainSettingsVM by viewModel()
    private val mapViewModel: MainMapVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        observeConnectionError(settingsViewModel.connectionError, binding.root) {
            settingsViewModel.retry()
        }
        observeConnectionError(mapViewModel.connectionError, binding.root) {
            settingsViewModel.retry()
        }
    }

    private fun initViews() {
        navController = findNavController(R.id.mainNavHostFragment)
        NavigationUI.setupWithNavController(binding.mainBottomNavView, navController)
    }
}