package com.hpk.domain.repositories

interface AuthRepository {
    suspend fun getAnonymousToken(): String

    suspend fun getCurrentToken(): String

}
