package com.hpk.fuelmap.features.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.ui.base.BaseActivity
import com.hpk.fuelmap.databinding.ActivityMainBinding
import com.hpk.fuelmap.features.main.map.FuelsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainVM by viewModel()
    private val adapter = FuelsAdapter(this)
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            applicationContext.getSharedPreferences("launch", Context.MODE_PRIVATE)
        initViews()
        checkLaunch()
        observeStationData()
        observeConnectionError(viewModel.connectionError, binding.root) {
            viewModel.retry()
        }
        observeLoading(viewModel.isLoading)
        observeErrorMessage(binding.errorContainer, viewModel.errorMessage)
    }

    private fun initViews() {
        loadingContainer = binding.loadingContainer.root
        navController = findNavController(R.id.mainNavHostFragment)
        NavigationUI.setupWithNavController(binding.mainBottomNavView, navController)
        initBottomSheet()

    }

    private fun initBottomSheet() {
        viewModel.stationData.observe(this) { stationData ->
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(R.layout.bottom_sheet_dialog)
            val company = dialog.findViewById<TextView>(R.id.companyTV)
            val workState = dialog.findViewById<TextView>(R.id.workStateTV)
            val fuelsList = dialog.findViewById<RecyclerView>(R.id.fuelsList)
            company?.text = stationData.company
            workState?.append(" ${stationData.workState}")
            fuelsList?.let {
                it.layoutManager = LinearLayoutManager(this)
                adapter.fuelsList = stationData.fuels
                it.adapter = adapter
            }
            adapter.onSubscribeClick = { fuel ->
                if (fuel.isSubscribed == true) {
                    viewModel.unsubscribeFromFuelUpdates(this, fuel.id)
                } else {
                    viewModel.subscribeOnFuelUpdates(this, fuel.id)
                }
                dialog.cancel()
            }
            dialog.show()
        }
    }

    private fun observeStationData() {
        viewModel.stationData.observe(this) {
            adapter.fuelsList = it.fuels
        }
    }

    private fun checkLaunch() {
        val isFirstStart = sharedPreferences.getBoolean(FIRST_LAUNCH, true)
        if (isFirstStart) {
            viewModel.registerDevice()
        }
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH, false).apply()
    }

    companion object {
        const val FIRST_LAUNCH = "first_launch"
    }
}