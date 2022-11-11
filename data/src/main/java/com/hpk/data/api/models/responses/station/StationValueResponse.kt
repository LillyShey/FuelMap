package com.hpk.data.api.models.responses.station

import com.google.gson.annotations.SerializedName
import com.hpk.data.api.models.responses.fuel.FuelResponse
import com.hpk.domain.models.ModelMapper
import com.hpk.domain.models.station.StationValue

class StationValueResponse(
    @SerializedName("company")
    val company: String,
    @SerializedName("work_state")
    val workState: String?,
    @SerializedName("fuels")
    val fuels: List<FuelResponse>?,
) {
    companion object : ModelMapper<StationValue, StationValueResponse> {
        override fun mapTo(model: StationValue): StationValueResponse {
            return StationValueResponse(
                company = model.company,
                workState = model.workState,
                fuels = model.fuels?.map { FuelResponse.mapTo(it) },
            )
        }

        override fun mapToDomain(model: StationValueResponse): StationValue {
            return StationValue(
                company = model.company,
                workState = model.workState,
                fuels = model.fuels?.map { FuelResponse.mapToDomain(it) },
            )
        }
    }
}