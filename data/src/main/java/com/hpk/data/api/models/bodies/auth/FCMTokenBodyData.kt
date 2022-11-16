package com.hpk.data.api.models.bodies.auth

import com.google.gson.annotations.SerializedName
import com.hpk.domain.models.ModelMapper
import com.hpk.domain.models.bodies.FCMTokenBody

class FCMTokenBodyData(
    @SerializedName("pushNotificationToken")
    val pushNotificationToken: String,
) {
    companion object : ModelMapper<FCMTokenBody, FCMTokenBodyData> {

        override fun mapTo(model: FCMTokenBody): FCMTokenBodyData {
            return FCMTokenBodyData(
                pushNotificationToken = model.pushNotificationToken,
            )
        }

        override fun mapToDomain(model: FCMTokenBodyData): FCMTokenBody {
            throw NotImplementedError()
        }

    }
}
