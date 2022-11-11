package com.hpk.data.repositories

import com.hpk.data.api.models.responses.fuel.FuelTypeResponse
import com.hpk.data.api.services.FuelTypeService
import com.hpk.data.extensions.mapToApiErrors
import com.hpk.data.providers.FuelTypeProvider
import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.repositories.FuelTypeRepository

class FuelTypeRepositoryImpl(
    private val fuelTypeService: FuelTypeService,
    private val fuelTypeProvider: FuelTypeProvider,
) : FuelTypeRepository {
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
                fuelTypeProvider.saveFuelTypesState((sharedPreferencesList+ onlyNew).filterIsInstance<FuelType>())
            }
            return fuelTypeProvider.getFuelTypesState()?.toList()
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun saveFuelTypeState(fuelTypes: List<FuelType>){
        fuelTypeProvider.saveFuelTypesState(fuelTypes)
    }
}