package com.hpk.data.api.authentificator

import com.google.gson.Gson
import com.hpk.data.BuildConfig
import com.hpk.data.api.RestConst
import com.hpk.data.api.models.bodies.auth.RefreshTokenBodyData
import com.hpk.data.api.models.responses.auth.AccessTokenResponse
import com.hpk.data.providers.TokenProvider
import com.hpk.data.api.services.AuthService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RefreshAuthenticator(
    private val accessTokenProvider: TokenProvider,
) : Authenticator {

    private var authToken: AccessTokenResponse? = null

    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {
        synchronized(this) {
            authToken = accessTokenProvider.provideAccessToken()
            val oldToken =
                response.request.header(RestConst.HEADER_AUTHORIZATION)?.replace("Bearer ", "")
                    ?: authToken?.token

            authToken?.let { tokenResponse ->
                var token = oldToken
                if (oldToken == tokenResponse.token) {
                    val accessTokenResponse = tryToRefreshToken(tokenResponse.refreshToken)
                    accessTokenResponse?.let {
                        accessTokenProvider.saveAccessToken(it)
                        token = accessTokenResponse.token
                    }
                }
                token?.let {
                    return response.request.newBuilder()
                        .header(RestConst.HEADER_AUTHORIZATION, "Bearer ${token ?: ""}")
                        .build()
                }
            } ?: kotlin.run {
                return null
            }
        }
    }

    private fun tryToRefreshToken(refreshToken: String?): AccessTokenResponse? {
        return runBlocking {
            if (refreshToken != null) {
                val refreshResponse: Response<AccessTokenResponse>
                try {
                    refreshResponse = attemptRefresh(refreshToken = refreshToken)

                    if (refreshResponse.isSuccessful) {
                        refreshResponse.body()?.let { accessTokenProvider.saveAccessToken(it) }
                        return@runBlocking refreshResponse.body()
                    } else {
                        return@runBlocking null
                    }
                } catch (e: IOException) {
                    throw e
                }
            } else {
                return@runBlocking null
            }
        }
    }

    private suspend fun attemptRefresh(refreshToken: String): Response<AccessTokenResponse> {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl(BuildConfig.API_BASE_URL + BuildConfig.API_VERSION)
            .client(getOkHttp())
            .build()

        return retrofit.create(AuthService::class.java).refreshToken(
            RefreshTokenBodyData(refreshToken)
        )
    }

    private fun getOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getInterceptor())
            .addInterceptor {
                val builder = it.request().newBuilder()
                    .url(it.request().url)
                it.proceed(builder.build())
            }
            .build()
    }

    private fun getInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }
}
