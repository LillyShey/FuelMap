package com.hpk.fuelmap.features.main

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
            dialog.show()
        }
    }
}