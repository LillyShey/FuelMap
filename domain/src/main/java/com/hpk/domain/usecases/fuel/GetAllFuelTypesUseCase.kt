package com.hpk.domain.usecases.fuel

import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.repositories.FuelRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllFuelTypesUseCase(private val fuelRepository: FuelRepository):BaseUseCase<List<FuelType>?, Unit>() {
    override suspend fun remoteWork(params: Unit?): List<FuelType>? {
        return withContext(Dispatchers.IO) {
            params.let {
                fuelRepository.getAllFuelTypes()
            }
        }
    }
}