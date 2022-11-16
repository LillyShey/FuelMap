package com.hpk.data.api.models.responses.auth

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AccessTokenResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String?
) : Serializable
