package com.hpk.fuelmap.features.main.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hpk.domain.models.fuel.FuelType
import com.hpk.fuelmap.databinding.ListItemFuelTypesBinding

class FuelTypesAdapter : RecyclerView.Adapter<FuelTypesAdapter.FuelTypesViewHolder>() {
    var fuelTypesList: List<FuelType>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onIsSwitcherChecked: ((FuelType, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuelTypesViewHolder {
        val binding = ListItemFuelTypesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return FuelTypesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FuelTypesViewHolder, position: Int) {
        fuelTypesList?.let {
            holder.onBind(it[position], onIsSwitcherChecked)
        }
    }

    override fun getItemCount() = fuelTypesList?.size ?: 0

    inner class FuelTypesViewHolder(private val binding: ListItemFuelTypesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: FuelType, onSwitcherChecked: ((FuelType, Boolean) -> Unit)?) {
            binding.fuelNameTV.text = item.name
            binding.fuelSwitch.isChecked = item.isChecked ?: false
            binding.fuelSwitch.setOnCheckedChangeListener { _, isChecked ->
                onSwitcherChecked?.invoke(item, isChecked)
            }
        }
    }
}