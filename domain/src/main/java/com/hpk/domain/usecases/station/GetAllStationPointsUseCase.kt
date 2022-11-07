package com.hpk.domain.usecases.station

import com.hpk.domain.models.station.Station
import com.hpk.domain.repositories.StationRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllStationPointsUseCase(private val stationRepository: StationRepository) :
    BaseUseCase<List<Station>, GetAllStationPointsUseCase.Params>() {
    override suspend fun remoteWork(params: Params?): List<Station> {
        return withContext(Dispatchers.IO) {
            stationRepository.getAllStationPoints(params!!.southWestLatitude,
                params.southWestLongitude,
                params.northEastLatitude,
                params.northEastLongitude,
                params.filter)
        }
    }

    class Params(
        val southWestLatitude: Double?,
        val southWestLongitude: Double?,
        val northEastLatitude: Double?,
        val northEastLongitude: Double?,
        val filter: String?,
    )
}