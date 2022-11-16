package com.hpk.data.api.services

import com.hpk.data.api.models.bodies.auth.FCMTokenBodyData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface FuelService {
    @POST("fuel/subscribe/{fuel}")
    suspend fun subscribeOnUpdates(
        @Path("fuel") fuel: String,
        @Body fcmTokenBodyData: FCMTokenBodyData,
    )

    @POST("fuel/unsubscribe/{fuel}")
    suspend fun unsubscribeFromFuelUpdates(
        @Path("fuel") fuel: String,
        @Body fcmTokenBodyData: FCMTokenBodyData,
    ):Response<Unit>
}