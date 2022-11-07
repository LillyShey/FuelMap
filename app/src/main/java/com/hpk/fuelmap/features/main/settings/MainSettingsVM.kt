package com.hpk.fuelmap.features.main.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.usecases.fuel.GetAllFuelTypesUseCase
import com.hpk.domain.usecases.base.ResultCallbacks
import com.hpk.fuelmap.common.ui.base.BaseViewModel

class MainSettingsVM(
    private val getAllFuelTypesUseCase: GetAllFuelTypesUseCase,
) : BaseViewModel() {
    val fuelTypes = MutableLiveData<List<FuelType>?>()

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
}