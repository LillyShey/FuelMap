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

    var onSubscribeClick: ((Fuel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuelsViewHolder {
        val binding = ListItemFuelsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return FuelsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FuelsViewHolder, position: Int) {
        fuelsList?.let {
            holder.onBind(it[position], onSubscribeClick)
        }
    }

    override fun getItemCount() = fuelsList?.size ?: 0

    inner class FuelsViewHolder(private val binding: ListItemFuelsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(item: Fuel, onButtonClick: ((Fuel) -> Unit)?) {
            binding.fuelNameTV.text = context.getString(R.string.fuel_name, "Невідома")
            binding.fuelBrandTV.text = context.getString(R.string.fuel_brand, "Невідомий")
            binding.specialTV.text = context.getString(R.string.fuel_speciality, "Невідомий")
            binding.priceTV.text =
                context.getString(R.string.fuel_price, "0")
            binding.availabilityIV.setBackgroundResource(R.drawable.icon_not_available)
            binding.subscribeBtn.setBackgroundResource(R.drawable.subscribe_btn_bg)
            binding.subscribeBtn.text = context.getString(R.string.subscribe_button)

            item.name?.let {
                binding.fuelNameTV.text = context.getString(R.string.fuel_name, item.name)
            }
            item.brand?.let {
                binding.fuelBrandTV.text = context.getString(R.string.fuel_brand, item.brand)
            }
            item.special?.let {
                binding.specialTV.text = context.getString(R.string.fuel_speciality, item.special)
            }
            item.availability?.let {
                if (item.availability == true) {
                    binding.availabilityIV.setBackgroundResource(R.drawable.icon_available)
                }
            }
            item.price?.let {
                binding.priceTV.text =
                    context.getString(R.string.fuel_price,
                        (item.price?.let { it / 100.0 }).toString())
            }
            item.isSubscribed?.let {
                if (it) {
                    binding.subscribeBtn.setBackgroundResource(R.drawable.unsubscribe_btn_bg)
                    binding.subscribeBtn.text = context.getString(R.string.unsubscribe_button)
                }
            }
            binding.subscribeBtn.setOnClickListener {
                if (it.isPressed) {
                    onButtonClick?.invoke(item)
                }
            }
        }
    }
}