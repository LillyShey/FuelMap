package com.hpk.domain.repositories

import com.hpk.domain.models.bodies.RegisterDeviceBody

interface DeviceRepository {
    suspend fun registerDevice(body: RegisterDeviceBody)
    suspend fun unregisterDevice()
}
