package com.hpk.domain.di

import com.hpk.domain.usecases.AuthIfNeededUseCase
import com.hpk.domain.usecases.GetAllFuelTypesUseCase
import com.hpk.domain.usecases.SaveFuelTypeStateUseCase
import com.hpk.domain.usecases.location.GetCurrentLocationUseCase
import org.koin.dsl.*

private val useCaseModule = module {
    factory<GetAllFuelTypesUseCase>()
    factory<GetCurrentLocationUseCase>()
    factory<SaveFuelTypeStateUseCase>()
    factory<AuthIfNeededUseCase>()
}

val domainModule = arrayOf(useCaseModule)