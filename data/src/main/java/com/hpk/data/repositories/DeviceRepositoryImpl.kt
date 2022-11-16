package com.hpk.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import com.hpk.data.api.models.bodies.auth.RegisterDeviceBodyData
import com.hpk.data.api.services.DeviceService
import com.hpk.data.extensions.mapToApiErrors
import com.hpk.domain.models.bodies.RegisterDeviceBody
import com.hpk.domain.repositories.DeviceRepository

class DeviceRepositoryImpl(
    private val deviceService: DeviceService,
    private val context: Context
) : DeviceRepository {
    companion object {
        private const val KEY_PUSH_NTF_TOKEN = "pushNotificationToken"
    }

    private val sharedPreferences = context.getSharedPreferences(
        "device_prefs",
        Context.MODE_PRIVATE
    )

    @SuppressLint("ApplySharedPref")
    override suspend fun registerDevice(body: RegisterDeviceBody) {
        try {
            sharedPreferences.edit().putString(KEY_PUSH_NTF_TOKEN, body.pushNotificationToken)
                .commit()
            deviceService.registerDevice(RegisterDeviceBodyData.mapTo(body))
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun unregisterDevice() {
        try {
            sharedPreferences.getString(KEY_PUSH_NTF_TOKEN, null)?.let {
                deviceService.unregisterDevice(it)
            }
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }
}
