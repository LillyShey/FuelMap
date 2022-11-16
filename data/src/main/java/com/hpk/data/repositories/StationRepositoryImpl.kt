package com.hpk.data.repositories

import com.hpk.data.api.models.responses.station.StationResponse
import com.hpk.data.api.models.responses.station.StationValueResponse
import com.hpk.data.api.services.StationService
import com.hpk.data.extensions.mapToApiErrors
import com.hpk.domain.models.station.Station
import com.hpk.domain.models.station.StationValue
import com.hpk.domain.repositories.StationRepository

class StationRepositoryImpl(private val stationService: StationService) : StationRepository {
    override suspend fun getAllStationPoints(
        southWestLatitude: Double?,
        southWestLongitude: Double?,
        northEastLatitude: Double?,
        northEastLongitude: Double?,
        filter: String?,
    ): List<Station> {
        try {
            return stationService.getAllStationPoints(southWestLatitude,
                southWestLongitude,
                northEastLatitude,
                northEastLongitude,
                filter)
                .map { station -> StationResponse.mapToDomain(station) }
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun getStationData(id: String, fcmToken: String): StationValue {
        try {
            return StationValueResponse.mapToDomain(stationService.getStationData(id, fcmToken))
        } catch (e: Throwable) {
            throw e.mapToApiErrors()
        }
    }
}