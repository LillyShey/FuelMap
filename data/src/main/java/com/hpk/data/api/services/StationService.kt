package com.hpk.data.api.services

import com.hpk.data.api.models.responses.station.StationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StationService {
    @GET("stations")
    suspend fun getAllStationPoints(
        @Query("southWest[latitude]") southWestLatitude: Double?,
        @Query("southWest[longitude]") southWestLongitude: Double?,
        @Query("northEast[latitude]") northEastLatitude: Double?,
        @Query("northEast[longitude]") northEastLongitude: Double?,
        @Query("filter[fuels]") filter:String?,
    ): List<StationResponse>
}