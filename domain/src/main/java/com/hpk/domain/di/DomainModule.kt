package com.hpk.domain.di

import com.hpk.domain.usecases.auth.*
import com.hpk.domain.usecases.fuel.*
import com.hpk.domain.usecases.location.*
import com.hpk.domain.usecases.station.*
import org.koin.dsl.factory
import org.koin.dsl.module

private val useCaseModule = module {
    factory<GetAllFuelTypesUseCase>()
    factory<SaveFuelTypeStateUseCase>()
    factory<AuthIfNeededUseCase>()
    factory<GetCurrentLocationUseCase>()
    factory<GetAllStationPointsUseCase>()
    factory<GetStationDataUseCase>()
}

val domainModule = arrayOf(useCaseModule)