package com.hpk.data.repositories

import com.hpk.data.api.models.responses.fuel.FuelTypeResponse
import com.hpk.data.extensions.mapToApiErrors
import com.hpk.data.services.FuelTypeService
import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.repositories.FuelTypeRepository

class FuelTypeRepositoryImpl(private val fuelTypeService: FuelTypeService)
    : FuelTypeRepository {
    override suspend fun getAllFuelTypes(): List<FuelType> {
        try{
            /*val list=ArrayList<FuelType>()
            for(fuelType in fuelTypeService.getAllFuelTypes()){
                list.add(FuelTypeResponse.mapToDomain(fuelType))
            }*/
            val list = ArrayList<FuelType>()
            var fuel = FuelType("1", "95")
            list.add(fuel)
            fuel = FuelType("2", "92")
            list.add(fuel)
            fuel = FuelType("3", "ДП")
            list.add(fuel)
            fuel = FuelType("4", "Газ")
            list.add(fuel)
            return list
        }catch (e: Throwable){
            throw e.mapToApiErrors()
        }
    }
}