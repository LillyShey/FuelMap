package com.hpk.fuelmap.features.main.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hpk.domain.models.fuel.Fuel
import com.hpk.fuelmap.R
import com.hpk.fuelmap.databinding.ListItemFuelsBinding

class FuelsAdapter(private val context: Context) :
    RecyclerView.Adapter<FuelsAdapter.FuelsViewHolder>() {
    var fuelsList: List<Fuel>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuelsViewHolder {
        val binding = ListItemFuelsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return FuelsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FuelsViewHolder, position: Int) {
        fuelsList?.let {
            holder.onBind(it[position])
        }
    }

    override fun getItemCount() = fuelsList?.size ?: 0

    inner class FuelsViewHolder(private val binding: ListItemFuelsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(item: Fuel) {
            binding.fuelNameTV.text = context.getString(R.string.fuel_name, item.name)

            binding.fuelBrandTV.text = context.getString(R.string.fuel_brand, item.brand?.toString())
            binding.specialTV.text = context.getString(R.string.fuel_speciality, item.special)
            if (item.availability == true) {
                binding.availabilityIV.setBackgroundResource(R.drawable.icon_available)
            }
            if (item.availability == false) {
                binding.availabilityIV.setBackgroundResource(R.drawable.icon_not_available)
            }
            binding.priceTV.text =
                context.getString(R.string.fuel_price, (item.price?.let { it/100 }).toString())
        }
    }
}