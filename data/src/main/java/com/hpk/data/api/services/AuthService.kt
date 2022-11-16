package com.hpk.data.api.services

import com.hpk.data.api.RestConst
import com.hpk.data.api.models.bodies.auth.RefreshTokenBodyData
import com.hpk.data.api.models.responses.auth.AccessTokenResponse
import retrofit2.Response
import retrofit2.http.*

    interface AuthService {

    @GET("token")
    suspend fun getAnonymousToken(): AccessTokenResponse

    @POST("token/refresh")
    @Headers(RestConst.HEADER_CONTENT_TYPE_JSON)
    suspend fun refreshToken(@Body body: RefreshTokenBodyData): Response<AccessTokenResponse>
}
