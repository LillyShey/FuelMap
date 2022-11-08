package com.hpk.data.api.models.responses.station

import com.google.gson.annotations.SerializedName
import com.hpk.domain.models.ModelMapper
import com.hpk.domain.models.common.Coordinates
import com.hpk.domain.models.station.Station

class StationResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("coordinates")
    val coordinates: Coordinates,
) {
    companion object : ModelMapper<Station, StationResponse> {
        override fun mapTo(model: Station): StationResponse {
            return StationResponse(
                id = model.id,
                coordinates = model.coordinates
            )
        }

        override fun mapToDomain(model: StationResponse): Station {
            return Station(
                id = model.id,
                coordinates = model.coordinates
            )
        }
    }
}