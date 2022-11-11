package com.hpk.domain.models.station

import com.hpk.domain.models.fuel.Fuel

data class StationValue (
    val company: String,
    val workState: String?,
    val fuels: List<Fuel>?,
)