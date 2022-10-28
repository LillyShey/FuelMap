package com.hpk.fuelmap.features.main.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.hpk.domain.models.fuel.FuelType
import com.hpk.fuelmap.R

class FuelTypesAdapter(private val fuelTypesList: List<FuelType>?) :
    RecyclerView.Adapter<FuelTypesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_fuel_types, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fuel: FuelType
        fuelTypesList?.let {
            fuel = fuelTypesList[position]
            holder.fuelName.text = fuel.name
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        fuelTypesList?.let { size = it.size }
        return size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fuelName: TextView = itemView.findViewById(R.id.fuelNameTV)
        val fuelSwitch: SwitchCompat = itemView.findViewById(R.id.fuelSwitch)
    }
}