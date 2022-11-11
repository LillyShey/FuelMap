package com.hpk.domain.di

import com.hpk.domain.usecases.*
import org.koin.dsl.*

private val useCaseModule = module {
    factory<GetAllFuelTypesUseCase>()
    factory<SaveFuelTypeStateUseCase>()
    factory<AuthIfNeededUseCase>()
    factory<GetCurrentLocationUseCase>()
    factory<GetAllStationPointsUseCase>()
    factory<GetStationDataUseCase>()
}

val domainModule = arrayOf(useCaseModule)