package com.hpk.data.repositories

import com.hpk.data.api.models.bodies.auth.FCMTokenBodyData
import com.hpk.data.api.models.responses.fuel.FuelTypeResponse
import com.hpk.data.api.services.FuelService
import com.hpk.data.api.services.FuelTypeService
import com.hpk.data.extensions.mapToApiErrors
import com.hpk.data.providers.FuelTypeProvider
import com.hpk.domain.models.bodies.FCMTokenBody
import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.repositories.FuelRepository

class FuelRepositoryImpl(
    private val fuelTypeService: FuelTypeService,
    private val fuelTypeProvider: FuelTypeProvider,
    private val fuelService: FuelService,
) : FuelRepository {
    override suspend fun getAllFuelTypes(): List<FuelType>? {
        try {
            val sharedPreferencesList = fuelTypeProvider.getFuelTypesState()?.toList()
            val apiList = fuelTypeService.getAllFuelTypes()
                .map { fuelType -> FuelTypeResponse.mapToDomain(fuelType) }
            if (sharedPreferencesList.isNullOrEmpty()) {
                fuelTypeProvider.saveFuelTypesState(apiList)
            } else if (sharedPreferencesList.map { it.id } != apiList.map { it.id }) {
                val onlyNew = apiList.map { apiType ->
                    !sharedPreferencesList.map { sharedType -> sharedType.id }.contains(apiType.id)
                }
                fuelTypeProvider.saveFuelTypesState((sharedPreferencesList + onlyNew).filterIsInstance<FuelType>())
            }
            return fuelTypeProvider.getFuelTypesState()?.toList()
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun saveFuelTypeState(fuelTypes: List<FuelType>) {
        fuelTypeProvider.saveFuelTypesState(fuelTypes)
    }

    override suspend fun subscribeOnFuelUpdates(fuel: String, fcmTokenBody: FCMTokenBody) {
        try {
            fuelService.subscribeOnUpdates(fuel, FCMTokenBodyData.mapTo(fcmTokenBody))
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun unsubscribeFromFuelUpdates(fuel: String, fcmTokenBody: FCMTokenBody) {
        try {
            fuelService.unsubscribeFromFuelUpdates(fuel, FCMTokenBodyData.mapTo(fcmTokenBody))
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }
}