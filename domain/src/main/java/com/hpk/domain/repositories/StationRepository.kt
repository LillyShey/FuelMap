package com.hpk.domain.repositories

import com.hpk.domain.models.station.Station

interface StationRepository {
    suspend fun getAllStationPoints(
        southWestLatitude: Double?,
        southWestLongitude: Double?,
        northEastLatitude: Double?,
        northEastLongitude: Double?,
        filter: String?,
    ): List<Station>
}