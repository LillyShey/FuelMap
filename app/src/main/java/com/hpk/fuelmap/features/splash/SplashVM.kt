package com.hpk.fuelmap.features.splash

import androidx.lifecycle.viewModelScope
import com.hpk.domain.usecases.AuthIfNeededUseCase
import com.hpk.domain.usecases.base.ResultCallbacks
import com.hpk.fuelmap.common.ui.base.BaseViewModel

class SplashVM(
    private val authIfNeededUseCase: AuthIfNeededUseCase
) : BaseViewModel() {
    init {
        getCurrentToken()
    }
    private fun getCurrentToken() {
        authIfNeededUseCase(
            uiDispatcher = viewModelScope,
            result = ResultCallbacks(
                onSuccess = {
                    println(it)
                },
                onError = {
                    timber.log.Timber.e(it)
                    errorMessage.value = it.apiError?.toString()
                },
                onConnectionError = {
                    timber.log.Timber.e(it)
                    onConnectionError { getCurrentToken() }
                },
                onUnexpectedError = {
                    timber.log.Timber.e(it)
                    errorMessage.value = it.localizedMessage
                },
                onLoading = {
                    isLoading.value = it
                }
            )
        )
    }
}