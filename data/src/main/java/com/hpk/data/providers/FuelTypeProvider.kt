package com.hpk.data.providers

import android.content.Context
import com.google.gson.Gson
import com.hpk.domain.models.fuel.FuelType

class FuelTypeProvider(
    private val gson: Gson,
    appContext: Context,
) {
    companion object {
        private const val FUEL_TYPES_STATE = "fuel_types_state"
    }

    private val sharedPreferences = appContext.getSharedPreferences(
        "fuel_type_provider",
        Context.MODE_PRIVATE
    )

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun saveFuelTypesState(list: List<FuelType>) {
        sharedPreferences.edit().putString(
            FUEL_TYPES_STATE,
            gson.toJson(list)
        ).apply()
    }

    fun getFuelTypesState(): Array<FuelType>? = gson.fromJson(
        sharedPreferences.getString(FUEL_TYPES_STATE, null),
        Array<FuelType>::class.java
    )
}