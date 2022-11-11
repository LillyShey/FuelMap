package com.hpk.domain.usecases.station

import com.hpk.domain.models.station.StationValue
import com.hpk.domain.repositories.StationRepository
import com.hpk.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetStationDataUseCase(private val stationRepository: StationRepository) :
    BaseUseCase<StationValue, GetStationDataUseCase.Params>() {
    override suspend fun remoteWork(params: Params?): StationValue {
        return withContext(Dispatchers.IO) {
            stationRepository.getStationData(params!!.id)
        }
    }

    class Params(
        val  id: String,
    )
}