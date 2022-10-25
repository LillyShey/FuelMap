package com.hpk.data.extensions

import com.google.gson.Gson
import com.hpk.domain.exceptions.*
import com.hpk.data.api.models.errors.*
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.mapToApiErrors(handleUnAuthException: Boolean = false): Throwable {
    when (this) {
        is HttpException -> {
            return if (handleUnAuthException && (this.code() == 401 || this.code() == 403)) {
                UnauthorizedException()
            } else {
                val errorResponse = try {
                    Gson().fromJson(
                        this.response()?.errorBody()?.string(),
                        ApiErrorResponse::class.java
                    )
                } catch (e: Exception) {
                    ApiErrorResponse(errorDescription = "Api error")
                }
                if (errorResponse.error == "version_not_supported") {
                    NotSupportedVersionException(errorResponse?.toDomain())
                } else {
                    ApiErrorException(errorResponse?.toDomain())
                }
            }
        }
        is UnknownHostException,
        is SocketTimeoutException,
        is ConnectException,
        is TimeoutException -> return ConnectionErrorException()
        else -> return this
    }
}

fun Response<*>.handleApiError() {
    if (!isSuccessful) {
        val errorResponse = try {
            Gson().fromJson(
                this.errorBody()?.string(),
                ApiErrorResponse::class.java
            )
        } catch (e: Exception) {
            ApiErrorResponse(errorDescription = "Api error")
        }
        throw ApiErrorException(errorResponse?.toDomain())
    }
}
