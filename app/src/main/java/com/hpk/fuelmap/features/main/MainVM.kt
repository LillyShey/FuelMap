package com.hpk.fuelmap.features.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hpk.domain.models.common.Coordinates
import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.usecases.GetAllFuelTypesUseCase
import com.hpk.domain.usecases.SaveFuelTypeStateUseCase
import com.hpk.domain.usecases.base.LocationResultCallbacks
import com.hpk.domain.usecases.base.ResultCallbacks
import com.hpk.domain.usecases.location.GetCurrentLocationUseCase
import com.hpk.fuelmap.common.arch.SingleLiveEvent
import com.hpk.fuelmap.common.ui.base.BaseViewModel

class MainVM(
    private val getAllFuelTypesUseCase: GetAllFuelTypesUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val saveFuelTypeStateUseCase: SaveFuelTypeStateUseCase,
) : BaseViewModel() {
    val fuelTypes = MutableLiveData<List<FuelType>?>()
    val currentLocation = MutableLiveData<Coordinates?>()
    val noAnyLocationError = SingleLiveEvent<Unit>()


    fun getAllFuelsTypes() {
        getAllFuelTypesUseCase(
            uiDispatcher = viewModelScope,
            result = ResultCallbacks(
                onSuccess = {
                    fuelTypes.value = it
                },
                onError = {
                    fuelTypes.value = null
                    timber.log.Timber.e(it)
                    errorMessage.value = it.apiError?.toString()
                },
                onConnectionError = {
                    timber.log.Timber.e(it)
                    onConnectionError { getAllFuelsTypes() }
                },
                onUnexpectedError = {
                    fuelTypes.value = null
                    timber.log.Timber.e(it)
                    errorMessage.value = it.localizedMessage
                },
                onLoading = {
                    isLoading.value = it
                }
            )
        )
    }

    fun saveFuelTypeState(fuelType: FuelType) {
        saveFuelTypeStateUseCase(
            uiDispatcher = viewModelScope,
            params = SaveFuelTypeStateUseCase.Params(fuelType = fuelType),
            result = ResultCallbacks(
                onError = {
                    timber.log.Timber.e(it)
                    errorMessage.value = it.apiError?.toString()
                },
                onConnectionError = {
                    timber.log.Timber.e(it)
                    onConnectionError { getAllFuelsTypes() }
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