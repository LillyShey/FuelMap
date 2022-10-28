package com.hpk.domain.di

import com.hpk.domain.usecases.GetAllFuelTypesUseCase
import org.koin.dsl.*

private val useCaseModule = module {
    factory<GetAllFuelTypesUseCase>()
}

val domainModule = arrayOf(useCaseModule)