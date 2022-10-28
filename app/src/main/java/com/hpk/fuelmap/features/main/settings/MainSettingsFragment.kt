package com.hpk.fuelmap.features.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hpk.fuelmap.R
import com.hpk.fuelmap.databinding.FragmentMainSettingsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainSettingsFragment : Fragment() {

    private val binding: FragmentMainSettingsBinding by viewBinding(FragmentMainSettingsBinding::bind)
    private val viewModel: MainSettingsVM by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_main_settings, container, false)
    }

    override fun onStart() {
        super.onStart()
        initViews()
        observeOnFuelTypesListChange()
    }
    private fun initViews(){
        binding.fuelTypesRecycler.layoutManager = LinearLayoutManager(context)
    }

    private fun observeOnFuelTypesListChange(){
        viewModel.fuelTypes.observe(viewLifecycleOwner){
            val adapter = FuelTypesAdapter(it)
            binding.fuelTypesRecycler.adapter = adapter
        }
    }
}