package com.hpk.domain.di

import com.hpk.domain.usecases.SaveFuelTypeStateUseCase
import com.hpk.domain.usecases.fuel.GetAllFuelTypesUseCase
import com.hpk.domain.usecases.location.GetCurrentLocationUseCase
import com.hpk.domain.usecases.station.GetAllStationPointsUseCase
import com.hpk.domain.usecases.station.GetStationDataUseCase
import org.koin.dsl.*

private val useCaseModule = module {
    factory<GetAllFuelTypesUseCase>()
    factory<SaveFuelTypeStateUseCase>()
    factory<GetCurrentLocationUseCase>()
    factory<GetAllStationPointsUseCase>()
    factory<GetStationDataUseCase>()
}

val domainModule = arrayOf(useCaseModule)