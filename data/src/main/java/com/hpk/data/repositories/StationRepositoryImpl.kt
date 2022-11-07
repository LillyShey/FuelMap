package com.hpk.data.repositories

import com.hpk.data.api.models.responses.station.StationResponse
import com.hpk.data.api.services.StationService
import com.hpk.domain.models.station.Station
import com.hpk.domain.repositories.StationRepository

class StationRepositoryImpl(private val stationService: StationService) : StationRepository {
    override suspend fun getAllStationPoints(
        southWestLatitude: Double?,
        southWestLongitude: Double?,
        northEastLatitude: Double?,
        northEastLongitude: Double?,
        filter: String?,
    ): List<Station> {
        return stationService.getAllStationPoints(southWestLatitude,
            southWestLongitude,
            northEastLatitude,
            northEastLongitude,
            filter)
            .map { station -> StationResponse.mapToDomain(station) }
    }
}