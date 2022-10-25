package com.hpk.fuelmap.features.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.extensions.requestAppPermission
import com.hpk.fuelmap.common.ui.base.BaseActivity
import com.hpk.fuelmap.databinding.ActivityMainBinding
import android.Manifest
import splitties.toast.toast

class MainActivity : BaseActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        checkLocationPermission()
    }

    private fun initViews() {
        navController= findNavController( R.id.mainNavHostFragment)
        NavigationUI.setupWithNavController(binding.mainBottomNavView, navController)
    }
    private fun checkLocationPermission(){
        requestAppPermission {
            permission(Manifest.permission.ACCESS_BACKGROUND_LOCATION){
                granted {

                    toast("good location getting")
                    //onSuccess.invoke()
                }
                denied {
                    if (it.isPermanentlyDenied) {
                        toast(R.string.permission_permanently_denied_location_message)
                    }
                }
            }
        }
    }
}