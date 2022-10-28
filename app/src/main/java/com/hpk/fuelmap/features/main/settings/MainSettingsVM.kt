package com.hpk.fuelmap.features.main.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.usecases.GetAllFuelTypesUseCase
import com.hpk.domain.usecases.base.ResultCallbacks
import com.hpk.fuelmap.common.ui.base.BaseViewModel

class MainSettingsVM(
    private val getAllFuelTypesUseCase: GetAllFuelTypesUseCase,
) : BaseViewModel() {
    val fuelTypes = MutableLiveData<List<FuelType>?>()

    init {
        getAllFuelsTypes()
    }

    private fun getAllFuelsTypes() {
        getAllFuelTypesUseCase(
            uiDispatcher = viewModelScope,
            result = ResultCallbacks(
                onSuccess = {
                    fuelTypes.value = it
                },
                onError = {
                    Timber.e(it)
                    fuelTypes.value = null
                    errorMessage.value = it.message
                    onConnectionError { getAllFuelsTypes() }
                },
                onUnexpectedError = {

                    fuelTypes.value = null
                    errorMessage.value = it.message
                },
                onLoading = {
                    isLoading.value = it
                }
            )
        )
    }
}