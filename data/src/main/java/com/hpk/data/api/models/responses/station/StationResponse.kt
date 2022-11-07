package com.hpk.data.api.models.responses.station

import com.google.gson.annotations.SerializedName
import com.hpk.domain.models.ModelMapper
import com.hpk.domain.models.common.Coordinates
import com.hpk.domain.models.station.Station

class StationResponse(
    @SerializedName("station_id")
    val station_id: String,
    @SerializedName("coordinates")
    val coordinates: Coordinates,
) {
    companion object : ModelMapper<Station, StationResponse> {
        override fun mapTo(model: Station): StationResponse {
            return StationResponse(
                station_id = model.station_id,
                coordinates = model.coordinates
            )
        }

        override fun mapToDomain(model: StationResponse): Station {
            return Station(
                station_id = model.station_id,
                coordinates = model.coordinates
            )
        }
    }
}