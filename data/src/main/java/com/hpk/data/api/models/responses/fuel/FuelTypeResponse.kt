package com.hpk.data.api.models.responses.fuel

import com.google.gson.annotations.SerializedName
import com.hpk.domain.models.ModelMapper
import com.hpk.domain.models.fuel.FuelType

class FuelTypeResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String?,
) {
    companion object : ModelMapper<FuelType, FuelTypeResponse> {
        override fun mapTo(model: FuelType): FuelTypeResponse {
            return FuelTypeResponse(
                id = model.id,
                name = model.name
            )
        }

        override fun mapToDomain(model: FuelTypeResponse): FuelType {
            return FuelType(
                id = model.id,
                name = model.name
            )
        }
    }
}