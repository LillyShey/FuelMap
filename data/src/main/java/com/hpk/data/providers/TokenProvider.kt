package com.hpk.data.providers

import android.content.Context
import com.google.gson.Gson
import com.hpk.data.api.models.responses.auth.AccessTokenResponse

class TokenProvider(
    private val gson: Gson,
    appContext: Context
) {
    companion object {
        const val ACCESS_TOKEN_KEY = "access_token"
    }

    var onTokenCleared: (() -> Unit)? = null
    private val sharedPreferences = appContext.getSharedPreferences(
        "token_provider",
        Context.MODE_PRIVATE
    )

    fun saveAccessToken(accessTokenResponse: AccessTokenResponse) {
        sharedPreferences.edit().putString(
            ACCESS_TOKEN_KEY,
            gson.toJson(accessTokenResponse)
        ).apply()
    }

    fun provideAccessToken(): AccessTokenResponse? {
        return gson.fromJson(
            sharedPreferences.getString(ACCESS_TOKEN_KEY, null),
            AccessTokenResponse::class.java
        )
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
        onTokenCleared?.invoke()
    }
}
