package com.hpk.fuelmap.features.main.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hpk.domain.models.fuel.FuelType
import com.hpk.fuelmap.databinding.ListItemFuelTypesBinding

class FuelTypesAdapter : RecyclerView.Adapter<FuelTypesAdapter.ViewHolder>() {
    var fuelTypesList: List<FuelType>? = null
        get() = field
        set(value) {
            field = value
        }

    inner class ViewHolder(val binding: ListItemFuelTypesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemFuelTypesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fuelTypesList?.let {
            with(holder) {
                with(it[position]) {
                    binding.fuelNameTV.text = this.name
                    binding.fuelSwitch.isChecked = this.isChosen ?: false
                }
            }
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        fuelTypesList?.let { size = it.size }
        return size
    }
}