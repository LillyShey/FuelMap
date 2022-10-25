package com.hpk.domain.usecases

import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.repositories.FuelTypeRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllFuelTypesUseCase(private val fuelTypeRepository: FuelTypeRepository):BaseUseCase<List<FuelType>, Unit>() {
    override suspend fun remoteWork(params: Unit?): List<FuelType> {
        return withContext(Dispatchers.IO) {
            params.let {
                fuelTypeRepository.getAllFuelTypes()
            }
        }
    }
}