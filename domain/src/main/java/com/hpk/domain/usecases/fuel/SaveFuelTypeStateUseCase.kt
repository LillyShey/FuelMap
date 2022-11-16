package com.hpk.domain.usecases.fuel

import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.repositories.FuelRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveFuelTypeStateUseCase(private val fuelRepository: FuelRepository) :
    BaseUseCase<Unit, SaveFuelTypeStateUseCase.Params>() {
    override suspend fun remoteWork(params: Params?){
        return withContext(Dispatchers.IO) {
            params.let {
                fuelRepository.saveFuelTypeState(params!!.fuelTypes)
            }
        }
    }

    class Params(
        val fuelTypes: List<FuelType>
    )
}