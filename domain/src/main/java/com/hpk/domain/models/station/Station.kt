package com.hpk.domain.models.station

import com.hpk.domain.models.common.Coordinates

data class Station (
    val id: String,
    val coordinates: Coordinates
)