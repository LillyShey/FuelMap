package com.hpk.data.di

import org.koin.dsl.*

private val repositoriesModule=module{

}

private val apiServicesModule=module{

}

val dataModule = arrayOf(
    repositoriesModule,
    apiServicesModule,
)