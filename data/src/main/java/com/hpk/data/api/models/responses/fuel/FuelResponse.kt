package com.hpk.data.api.models.responses.fuel

import com.google.gson.annotations.SerializedName
import com.hpk.domain.models.ModelMapper
import com.hpk.domain.models.fuel.Fuel


class FuelResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name_with_brand")
    val name_with_brand: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("special")
    val special: String?,
    @SerializedName("availability")
    val availability: Boolean?,
    @SerializedName("price")
    val price: Int?,
) {

    companion object : ModelMapper<Fuel, FuelResponse> {
        override fun mapTo(model: Fuel): FuelResponse {
            return FuelResponse(
                id = model.id,
                name_with_brand = model.name_with_brand,
                name = model.name,
                brand = model.brand,
                special = model.special,
                availability = model.availability,
                price = model.price,
            )
        }

        override fun mapToDomain(model: FuelResponse): Fuel {
            return Fuel(
                id = model.id,
                name_with_brand = model.name_with_brand,
                name = model.name,
                brand = model.brand,
                special = model.special,
                availability = model.availability,
                price = model.price,
            )
        }
    }
}