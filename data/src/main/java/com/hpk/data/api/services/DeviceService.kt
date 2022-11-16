package com.hpk.data.api.services

import com.hpk.data.api.RestConst
import com.hpk.data.api.models.bodies.auth.RegisterDeviceBodyData
import retrofit2.Response
import retrofit2.http.*

interface DeviceService {
    @Headers(RestConst.HEADER_CONTENT_TYPE_JSON)
    @POST("devices/android")
    suspend fun registerDevice(@Body body: RegisterDeviceBodyData)

    @DELETE("devices/android/{pushNotificationToken}")
    suspend fun unregisterDevice(@Path("pushNotificationToken") pushNotificationToken: String): Response<Unit>

}
