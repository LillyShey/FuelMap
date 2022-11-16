package com.hpk.domain.repositories

import com.hpk.domain.models.station.Station
import com.hpk.domain.models.station.StationValue

interface StationRepository {
    suspend fun getAllStationPoints(
        southWestLatitude: Double?,
        southWestLongitude: Double?,
        northEastLatitude: Double?,
        northEastLongitude: Double?,
        filter: String?,
    ): List<Station>

    suspend fun getStationData(
        id: String,
        fcmToken: String
    ): StationValue
}