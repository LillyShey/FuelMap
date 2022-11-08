package com.hpk.data.repositories

import com.hpk.data.extensions.mapToApiErrors
import com.hpk.data.providers.TokenProvider
import com.hpk.data.services.AuthService
import com.hpk.domain.exceptions.UnauthorizedException
import com.hpk.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val tokenProvider: TokenProvider,
) : AuthRepository {
    override suspend fun getAnonymousToken(): String {
        try {
            val token = authService.getAnonymousToken()
            tokenProvider.saveAccessToken(token)
            return token.token
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun getCurrentToken(): String {
        return tokenProvider.provideAccessToken()?.token ?: getAnonymousToken()
    }

}
