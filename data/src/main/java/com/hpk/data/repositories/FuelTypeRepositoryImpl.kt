package com.hpk.data.repositories

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
            //FIXME Change to get data from API
            var list = ArrayList<FuelType>()
            if (fuelTypeProvider.getFuelTypesState().isEmpty()) {
//                for (fuelType in fuelTypeService.getAllFuelTypes()) {
//                    list.add(FuelTypeResponse.mapToDomain(fuelType))
//                }
                list.add(FuelType("1", "95"))
                list.add(FuelType("2", "92", true))
                list.add(FuelType("3", "ДП", true))
                list.add(FuelType("4", "Газ"))
                fuelTypeProvider.saveFuelTypesState(list)
            } else {
                list = fuelTypeProvider.getFuelTypesState().toList() as ArrayList<FuelType>
            }
            return list
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }
}