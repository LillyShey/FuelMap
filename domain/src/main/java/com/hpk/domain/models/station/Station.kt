package com.hpk.domain.models.station

import com.hpk.domain.models.common.Coordinates

data class Station (
    val station_id: String,
    val coordinates: Coordinates
)