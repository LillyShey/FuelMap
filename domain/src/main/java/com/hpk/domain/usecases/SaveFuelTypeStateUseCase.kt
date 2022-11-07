package com.hpk.domain.usecases

import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.repositories.FuelTypeRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveFuelTypeStateUseCase(private val fuelTypeRepository: FuelTypeRepository) :
    BaseUseCase<List<FuelType>, SaveFuelTypeStateUseCase.Params>() {
    override suspend fun remoteWork(params: Params?): List<FuelType> {
        return withContext(Dispatchers.IO) {
            params.let {
                fuelTypeRepository.saveFuelTypeState(params!!.fuelType, params.isChecked)
            }
        }
    }

    class Params(
        val fuelType: FuelType,
        val isChecked: Boolean,
    )
}