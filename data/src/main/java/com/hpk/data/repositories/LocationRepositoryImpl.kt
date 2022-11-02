package com.hpk.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.hpk.domain.exceptions.NoAnyLocationProvidersException
import com.hpk.domain.exceptions.NoLastLocationException
import com.hpk.domain.models.common.Coordinates
import com.hpk.domain.repositories.LocationRepository
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await

class LocationRepositoryImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest,
    private val context: Context,
) : LocationRepository {

    companion object {
        private const val LOCATION_ENABLE_DELAY = 2000L
    }

    @SuppressLint("MissingPermission")
    override suspend fun subscribeOnLocationChanges(): Flow<Coordinates> = channelFlow {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach {
                    offerCatching(
                        Coordinates(
                            it.latitude,
                            it.longitude
                        )
                    )
                }
            }
        }
        if (isLocationOn(true)) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
                .await()
            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        } else {
            throw NoAnyLocationProvidersException()
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): Coordinates {
        if (isLocationOn(false)) {
            fusedLocationProviderClient.lastLocation.await()?.let {
                return Coordinates(
                    it.latitude,
                    it.longitude
                )
            } ?: kotlin.run {
                throw NoLastLocationException()
            }
        } else {
            throw NoAnyLocationProvidersException()
        }
    }

    private suspend fun isLocationOn(withDelay: Boolean): Boolean {
        if (withDelay) {
            delay(LOCATION_ENABLE_DELAY)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            val lm =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else { // This is Deprecated in API 28
            @Suppress("DEPRECATION")
            val mode: Int = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }

    fun <E> SendChannel<E>.offerCatching(element: E): Boolean {
        return runCatching { trySend(element).isSuccess }.getOrDefault(false)
    }
}
