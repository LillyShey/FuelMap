package com.hpk.domain.di

import com.hpk.domain.usecases.GetAllFuelTypesUseCase
import com.hpk.domain.usecases.location.GetCurrentLocationUseCase
import org.koin.dsl.*

private val useCaseModule = module {
    factory<GetAllFuelTypesUseCase>()
    factory<GetCurrentLocationUseCase>()
}

val domainModule = arrayOf(useCaseModule)