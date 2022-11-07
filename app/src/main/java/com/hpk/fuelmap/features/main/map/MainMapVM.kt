package com.hpk.fuelmap.features.main.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.hpk.domain.models.common.Coordinates
import com.hpk.domain.models.station.Station
import com.hpk.domain.usecases.fuel.GetAllFuelTypesUseCase
import com.hpk.domain.usecases.base.LocationResultCallbacks
import com.hpk.domain.usecases.base.ResultCallbacks
import com.hpk.domain.usecases.location.GetCurrentLocationUseCase
import com.hpk.domain.usecases.station.GetAllStationPointsUseCase
import com.hpk.fuelmap.common.arch.SingleLiveEvent
import com.hpk.fuelmap.common.ui.base.BaseViewModel
import timber.log.Timber

class MainMapVM(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val gelAllStationPointsUseCase: GetAllStationPointsUseCase,
    private val getAllFuelTypesUseCase: GetAllFuelTypesUseCase,
) : BaseViewModel() {

    val currentLocation = MutableLiveData<Coordinates?>()
    val stations = MutableLiveData<List<Station>?>()
    private val checkedFuelTypes = MutableLiveData<String?>()
    private val latLngBounds = MutableLiveData<LatLngBounds>()
    val noAnyLocationError = SingleLiveEvent<Unit>()

    init {
        getCheckedFuelsTypes()
    }

    fun setLatLngBounds(latLngBounds: LatLngBounds) {
        this.latLngBounds.value = latLngBounds
        getAllStationPoints()
    }

    fun getCurrentLocation() {
        getCurrentLocationUseCase(
            uiDispatcher = viewModelScope,
            result = LocationResultCallbacks(
                onSuccess = {
                    currentLocation.value = it
                },
                onError = { errorMsg ->
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

    private fun getAllStationPoints() {
        gelAllStationPointsUseCase(
            uiDispatcher = viewModelScope,
            params = GetAllStationPointsUseCase.Params(
                southWestLatitude = latLngBounds.value?.southwest?.latitude,
                southWestLongitude = latLngBounds.value?.southwest?.longitude,
                northEastLatitude = latLngBounds.value?.northeast?.latitude,
                northEastLongitude = latLngBounds.value?.northeast?.longitude,
                filter = checkedFuelTypes.value
            ),
            result = ResultCallbacks(
                onSuccess = {
                    if(stations.value!=it){
                        stations.value = it
                    }
                },
                onError = {
                    stations.value = null
                    Timber.e(it)
                    errorMessage.value = it.apiError?.toString()
                },
                onConnectionError = {
                    Timber.e(it)
                    onConnectionError { getAllStationPoints() }
                },
                onUnexpectedError = {
                    stations.value = null
                    Timber.e(it)
                    errorMessage.value = it.localizedMessage
                },
                onLoading = {
                    isLoading.value = it
                }
            )
        )
    }

    private fun getCheckedFuelsTypes() {
        getAllFuelTypesUseCase(
            uiDispatcher = viewModelScope,
            result = ResultCallbacks(
                onSuccess = { list ->
                    checkedFuelTypes.value =
                        list.filter { it.isChecked == true }.joinToString(",") { it.id }
                },
                onError = {
                    checkedFuelTypes.value = null
                    Timber.e(it)
                    errorMessage.value = it.apiError?.toString()
                },
                onConnectionError = {
                    Timber.e(it)
                    onConnectionError { getCheckedFuelsTypes() }
                },
                onUnexpectedError = {
                    checkedFuelTypes.value = null
                    Timber.e(it)
                    errorMessage.value = it.localizedMessage
                },
                onLoading = {
                    isLoading.value = it
                }
            )
        )
    }
}