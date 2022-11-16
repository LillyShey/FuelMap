package com.hpk.data.api.models.bodies.auth

import com.google.gson.annotations.SerializedName
import com.hpk.domain.models.ModelMapper
import com.hpk.domain.models.bodies.RegisterDeviceBody

class RegisterDeviceBodyData(
    @SerializedName("pushNotificationToken")
    val pushNotificationToken: String,
    @SerializedName("androidVersion")
    val androidVersion: String,
    @SerializedName("vendor")
    val vendor: String
) {
    companion object : ModelMapper<RegisterDeviceBody, RegisterDeviceBodyData> {

        override fun mapTo(model: RegisterDeviceBody): RegisterDeviceBodyData {
            return RegisterDeviceBodyData(
                pushNotificationToken = model.pushNotificationToken,
                androidVersion = model.androidVersion,
                vendor = model.vendor
            )
        }

        override fun mapToDomain(model: RegisterDeviceBodyData): RegisterDeviceBody {
            throw NotImplementedError()
        }

    }
}
