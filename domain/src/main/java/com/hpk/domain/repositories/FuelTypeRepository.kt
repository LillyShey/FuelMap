package com.hpk.domain.repositories

import com.hpk.domain.models.fuel.FuelType

interface FuelTypeRepository {
    suspend fun getAllFuelTypes(): List<FuelType>
    suspend fun saveFuelTypeState(fuelTypes: List<FuelType>)
}