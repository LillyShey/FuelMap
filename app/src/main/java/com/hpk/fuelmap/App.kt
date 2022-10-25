package com.hpk.fuelmap

import androidx.multidex.MultiDexApplication
import com.hpk.domain.di.domainModule
import com.hpk.data.di.dataModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class App: MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI(){
        GlobalContext.startKoin{
            androidLogger(if(BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(
                *dataModule,
                *domainModule,
            )
        }
    }
}