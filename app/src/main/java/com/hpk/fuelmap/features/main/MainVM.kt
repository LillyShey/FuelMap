package com.hpk.fuelmap.features.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.hpk.domain.models.common.Coordinates
import com.hpk.domain.models.fuel.FuelType
import com.hpk.domain.models.station.Station
import com.hpk.domain.models.station.StationValue
import com.hpk.domain.usecases.SaveFuelTypeStateUseCase
import com.hpk.domain.usecases.base.LocationResultCallbacks
import com.hpk.domain.usecases.base.ResultCallbacks
import com.hpk.domain.usecases.fuel.GetAllFuelTypesUseCase
import com.hpk.domain.usecases.location.GetCurrentLocationUseCase
import com.hpk.domain.usecases.station.GetAllStationPointsUseCase
import com.hpk.domain.usecases.station.GetStationDataUseCase
import com.hpk.fuelmap.common.arch.SingleLiveEvent
import com.hpk.fuelmap.common.extensions.notifyValueChange
import com.hpk.fuelmap.common.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

class MainVM(
    private val getAllFuelTypesUseCase: GetAllFuelTypesUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val saveFuelTypeStateUseCase: SaveFuelTypeStateUseCase,
    private val getAllStationPointsUseCase: GetAllStationPointsUseCase,
    private val getStationDataUseCase: GetStationDataUseCase,
) : BaseViewModel() {
    val fuelTypes = MutableLiveData<MutableList<FuelType>?>()
    val currentLocation = MutableLiveData<Coordinates?>()
    val noAnyLocationError = SingleLiveEvent<Unit>()
    val stations = MutableLiveData<List<Station>?>()
    val stationData = MutableLiveData<StationValue>()
    private val checkedFuelTypes = MutableLiveData<String?>()
    private val latLngBounds = MutableLiveData<LatLngBounds>()

    init {
        getAllFuelsTypes()
    }

    fun setLatLngBounds(latLngBounds: LatLngBounds) {
        this.latLngBounds.value = latLngBounds
        getAllStationPoints()
    }

    private fun getAllFuelsTypes() {
        getAllFuelTypesUseCase(
            uiDispatcher = viewModelScope,
            result = ResultCallbacks(
                onSuccess = {
                    fuelTypes.value = it?.toMutableList()
                },
                onError = {
                    fuelTypes.value = null
                    Timber.e(it)
                    errorMessage.value = it.apiError?.toString()
                },
                onConnectionError = {
                    Timber.e(it)
                    onConnectionError { getAllFuelsTypes() }
                },
                onUnexpectedError = {
                    fuelTypes.value = null
                    Timber.e(it)
                    errorMessage.value = it.localizedMessage
                },
                onLoading = {
                    isLoading.value = it
                }
            )
        )
    }

    fun saveFuelTypeState(fuelType: FuelType, isChecked: Boolean) {
        fuelTypes.value?.firstOrNull { it.id == fuelType.id }?.isChecked = isChecked
        fuelTypes.notifyValueChange()
        fuelTypes.value?.let { list ->
            saveFuelTypeStateUseCase(
                uiDispatcher = viewModelScope,
                params = SaveFuelTypeStateUseCase.Params(fuelTypes = list),
                result = ResultCallbacks(
                    onError = {
                        Timber.e(it)
                        errorMessage.value = it.apiError?.toString()
                    },
                    onConnectionError = {
                        Timber.e(it)
                        onConnectionError { getAllFuelsTypes() }
                    },
                    onUnexpectedError = {
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


    @OptIn(ExperimentalCoroutinesApi::class)
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
        getAllStationPointsUseCase(
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
                    if (stations.value != it) {
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
                        list?.filter { it.isChecked == true }?.joinToString(",") { it.id }
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

    fun getStationData(id: String) {
        getStationDataUseCase(
            uiDispatcher = viewModelScope,
            params = GetStationDataUseCase.Params(id = id),
            result = ResultCallbacks(
                onSuccess = {
                    stationData.value = it
                },
                onError = {
                    Timber.e(it)
                    errorMessage.value = it.apiError?.toString()
                },
                onConnectionError = {
                    Timber.e(it)
                    onConnectionError { getCheckedFuelsTypes() }
                },
                onUnexpectedError = {
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