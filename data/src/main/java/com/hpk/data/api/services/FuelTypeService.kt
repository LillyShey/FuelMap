package com.hpk.data.api.services

import com.hpk.data.api.models.responses.fuel.FuelTypeResponse
import retrofit2.http.GET

interface FuelTypeService {
    @GET("fuel/types")
    suspend fun getAllFuelTypes(): List<FuelTypeResponse>
}