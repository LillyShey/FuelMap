    package com.hpk.data.api.models.bodies.auth

    import com.google.gson.annotations.SerializedName

    class RefreshTokenBodyData (
        @SerializedName("refreshToken")
        val refreshToken: String
    )
