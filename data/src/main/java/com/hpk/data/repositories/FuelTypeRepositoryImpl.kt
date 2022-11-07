package com.hpk.data.repositories

import com.hpk.data.api.models.responses.fuel.FuelTypeResponse
import com.hpk.data.extensions.mapToApiErrors
import com.hpk.data.providers.FuelTypeProvider
import com.hpk.data.services.FuelTypeService
import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.repositories.FuelTypeRepository

class FuelTypeRepositoryImpl(
    private val fuelTypeService: FuelTypeService,
    private val fuelTypeProvider: FuelTypeProvider,
) : FuelTypeRepository {
    override suspend fun getAllFuelTypes(): List<FuelType> {
        try {
            val sharedPreferencesList = fuelTypeProvider.getFuelTypesState()
            val apiList = fuelTypeService.getAllFuelTypes()
                .map { fuelType -> FuelTypeResponse.mapToDomain(fuelType) }
            if (sharedPreferencesList.isEmpty()) {
                fuelTypeProvider.saveFuelTypesState(apiList)
            } else if (sharedPreferencesList.map { it.id } != apiList.map { it.id }) {
                val onlyNew = apiList.map { apiType ->
                    !sharedPreferencesList.map { sharedType -> sharedType.id }.contains(apiType.id)
                }
                fuelTypeProvider.saveFuelTypesState((sharedPreferencesList + onlyNew).filterIsInstance<FuelType>())
            }
            return fuelTypeProvider.getFuelTypesState()
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun saveFuelTypeState(fuelType: FuelType){
        val sharedPreferencesList = fuelTypeProvider.getFuelTypesState() as ArrayList<FuelType>
        sharedPreferencesList[sharedPreferencesList.indexOfFirst { it.id == fuelType.id }] = fuelType
        fuelTypeProvider.saveFuelTypesState(sharedPreferencesList)
    }
}