package com.hpk.fuelmap.features.main.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hpk.fuelmap.R
import com.hpk.fuelmap.common.ui.base.BaseFragment
import com.hpk.fuelmap.databinding.FragmentMainSettingsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainSettingsFragment : BaseFragment(R.layout.fragment_main_settings) {

    private val binding: FragmentMainSettingsBinding by viewBinding(FragmentMainSettingsBinding::bind)
    private val sharedViewModel: MainSettingsVM by sharedViewModel()
    private val listAdapter = FuelTypesAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLoading(sharedViewModel.isLoading)
        observeErrorMessage(binding.errorContainer, sharedViewModel.errorMessage)
        observeOnFuelTypesListChange()
    }

    override fun onStart() {
        super.onStart()
        observeOnFuelTypesListChange()
    }

    private fun initViews() {
        binding.fuelTypesRecycler.layoutManager = LinearLayoutManager(context)
        binding.fuelTypesRecycler.adapter = listAdapter
    }

    private fun observeOnFuelTypesListChange() {
        sharedViewModel.fuelTypes.observe(viewLifecycleOwner) { fuelTypeList ->
            listAdapter.fuelTypesList = fuelTypeList
        }
    }
}