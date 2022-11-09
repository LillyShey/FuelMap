package com.hpk.domain.models

interface ModelMapper<DOMAIN, TO> {
    fun mapTo(model: DOMAIN): TO
    fun mapToDomain(model: TO): DOMAIN
}
