package com.hpk.fuelmap.features.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.ui.base.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openMainActivity()
    }
    private fun openMainActivity() {
        findNavController().navigate(R.id.action_splashFragment_to_mainMapFragment)
    }
}