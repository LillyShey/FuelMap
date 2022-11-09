package com.hpk.fuelmap.features.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.ui.base.BaseActivity
import com.hpk.fuelmap.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        observeConnectionError(viewModel.connectionError, binding.root) {
            viewModel.retry()
        }
        observeLoading(viewModel.isLoading)
        observeErrorMessage(binding.errorContainer, viewModel.errorMessage)
    }

    private fun initViews() {
        navController = findNavController(R.id.mainNavHostFragment)
        NavigationUI.setupWithNavController(binding.mainBottomNavView, navController)
    }

}