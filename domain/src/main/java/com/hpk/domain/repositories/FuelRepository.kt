package com.hpk.domain.repositories

import com.hpk.domain.models.bodies.FCMTokenBody
import com.hpk.domain.models.fuel.FuelType

interface FuelRepository {
    suspend fun getAllFuelTypes(): List<FuelType>?

    suspend fun saveFuelTypeState(fuelTypes: List<FuelType>)

    suspend fun subscribeOnFuelUpdates(fuel: String, fcmTokenBody: FCMTokenBody)

    suspend fun unsubscribeFromFuelUpdates(fuel: String, fcmTokenBody: FCMTokenBody)
}