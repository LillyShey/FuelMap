package com.hpk.fuelmap.features.main.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hpk.domain.models.common.Coordinates
import com.hpk.domain.usecases.base.LocationResultCallbacks
import com.hpk.domain.usecases.location.GetCurrentLocationUseCase
import com.hpk.fuelmap.common.arch.SingleLiveEvent
import com.hpk.fuelmap.common.ui.base.BaseViewModel
import timber.log.Timber

class MainMapVM(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : BaseViewModel() {

    val currentLocation = MutableLiveData<Coordinates?>()
    val noAnyLocationError = SingleLiveEvent<Unit>()

    fun getCurrentLocation() {
        getCurrentLocationUseCase(
            uiDispatcher = viewModelScope,
            result = LocationResultCallbacks(
                onSuccess = {
                    currentLocation.value = it
                },
                onError = {errorMsg->
                    errorMessage.value = errorMsg
                },
                onNoAnyLocationProvider = {
                    noAnyLocationError.call()
                },
                isLoading = { loading ->
                    isLoading.value = loading
                }
            )
        )
    }
}