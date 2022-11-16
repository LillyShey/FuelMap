package com.hpk.domain.usecases.device

import com.hpk.domain.models.bodies.RegisterDeviceBody
import com.hpk.domain.repositories.DeviceRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterDeviceUseCase(
    private val deviceRepository: DeviceRepository,
) : BaseUseCase<Unit, RegisterDeviceUseCase.Params>() {

    override suspend fun remoteWork(params: Params?) {
        return withContext(Dispatchers.IO) {
            deviceRepository.registerDevice(params!!.body)
        }
    }

    class Params(
        val body: RegisterDeviceBody,
    )

}
