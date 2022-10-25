package com.hpk.domain.models.common

data class ListMetadata(
    val limit: Int?,
    val total: Int?,
    val offset: Int? = null,
    val sinceId: String? = null
)
