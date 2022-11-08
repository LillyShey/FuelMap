package com.hpk.fuelmap.features.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.ui.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    companion object {
        private const val DELAY_SPLASH = 1500L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(DELAY_SPLASH)
            observeData()
        }
    }

    private fun observeData() {
        openMainActivity()
    }

    private fun openMainActivity() {
        findNavController().navigate(R.id.action_splashFragment_to_mainMapFragment)
    }
}