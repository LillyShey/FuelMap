package com.hpk.domain.models.fuel


data class Fuel (
    val id: String,
    val name_with_brand: String?,
    val name: String?,
    val brand: String?,
    val special:String?,
    val availability: Boolean?,
    val price: Int?
)