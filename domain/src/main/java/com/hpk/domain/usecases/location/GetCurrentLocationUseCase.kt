package com.hpk.domain.usecases.location

import com.hpk.domain.exceptions.NoAnyLocationProvidersException
import com.hpk.domain.exceptions.NoLastLocationException
import com.hpk.domain.models.common.Coordinates
import com.hpk.domain.repositories.LocationRepository
import com.hpk.domain.usecases.base.LocationResultCallbacks
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.take

class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository,
) {
    @ExperimentalCoroutinesApi
    operator fun invoke(
        uiDispatcher: CoroutineScope,
        result: LocationResultCallbacks<Coordinates?>,
    ) {
        result.isLoading?.invoke(true)
        uiDispatcher.launch {
            try {
                withContext(Dispatchers.Main) {
                    result.onSuccess?.invoke(locationRepository.getLastKnownLocation())
                    result.isLoading?.invoke(false)
                }
            } catch (e: NoLastLocationException) {
                withContext(Dispatchers.Main) {
                    locationRepository.subscribeOnLocationChanges().drop(1).take(1).collect {
                        result.isLoading?.invoke(false)
                        result.onSuccess?.invoke(it)
                    }
                }
            } catch (noAnyLocationProvider: NoAnyLocationProvidersException) {
                result.onNoAnyLocationProvider?.invoke(Unit)
                result.isLoading?.invoke(false)
            } catch (e: Throwable) {
                result.isLoading?.invoke(false)
                result.onError?.invoke(e.localizedMessage)
            }
        }
    }
}
