package com.hpk.domain.usecases

import com.hpk.domain.repositories.AuthRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthIfNeededUseCase(private val authRepository: AuthRepository) :
    BaseUseCase<String, Unit>() {
    override suspend fun remoteWork(params: Unit?): String {
        return withContext(Dispatchers.IO) {
            authRepository.getCurrentToken()
        }
    }
}