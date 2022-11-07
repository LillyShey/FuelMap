package com.hpk.fuelmap.common.di

import com.hpk.fuelmap.features.main.MainVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModel<MainVM>()
}


val appModule = arrayOf(
    viewModelsModule,
)
