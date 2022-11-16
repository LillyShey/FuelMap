package com.hpk.domain.usecases.fuel

import com.hpk.domain.models.bodies.FCMTokenBody
import com.hpk.domain.repositories.FuelRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubscribeOnFuelUpdatesUseCase(private val fuelRepository: FuelRepository) :
    BaseUseCase<Unit, SubscribeOnFuelUpdatesUseCase.Params>() {

    override suspend fun remoteWork(params: Params?) {
        return withContext(Dispatchers.IO) {
            fuelRepository.subscribeOnFuelUpdates(params!!.fuel, params.fcmTokenBody)
        }
    }

    class Params(
        val fuel: String,
        val fcmTokenBody: FCMTokenBody,
    )
}