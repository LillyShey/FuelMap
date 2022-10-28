package com.hpk.fuelmap.common.di

import com.hpk.fuelmap.features.main.settings.MainSettingsVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModel<MainSettingsVM>()
}


val appModule = arrayOf(
    viewModelsModule,
)
