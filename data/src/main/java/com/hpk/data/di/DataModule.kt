package com.hpk.data.di

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hpk.data.BuildConfig
import com.hpk.data.api.RestConst
import com.hpk.data.providers.*
import com.hpk.data.repositories.*
import com.hpk.data.api.services.*
import com.hpk.domain.repositories.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val repositoriesModule = module {
    single<FuelTypeRepositoryImpl>() bind FuelTypeRepository::class
    single<LocationRepositoryImpl>() bind LocationRepository::class
    single<StationRepositoryImpl>() bind StationRepository::class
}
private val locationModule = module {
    single {
        LocationServices.getFusedLocationProviderClient(
            androidContext()
        )
    }
    single { androidApplication().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    factory {
        LocationRequest().apply {
            interval = 5000
            fastestInterval = 2500
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
    }
}
private val apiServicesModule = module {
    single<FuelTypeService> { (get() as Retrofit).create(FuelTypeService::class.java) }
    single<StationService> { (get() as Retrofit).create(StationService::class.java) }
}
private val networkModule = module {
    factory<Gson> {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
            .create()
    }

    factory<GsonConverterFactory> { GsonConverterFactory.create(get()) }

    factory {
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    factory {
        Interceptor {
            val builder = it.request().newBuilder()
                .url(it.request().url)
            builder.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2NjgwMDAyNjcsImV4cCI6MTY2ODA4NjY2Nywicm9sZXMiOlsiUk9MRV9BTk9OWU1PVVMiXSwidXNlcm5hbWUiOiJhbm9ueW1vdXMifQ.h0RkyCLa4dxdoqkoq6YImINvOPaZYytiyadth0q_yNaBK3mR5qrGVUPo7epAMR__ZRI2kyH4Z3M3xUxP2T_4OyHwFM_amy6Ng2JPUJYtfvjAMcRAn6KxyajT2gZAn6n1FED1j1e7gn3XnfYWOh7ZDRsYMZWRHAcImYeSECzyAz_ySul-CEDgnaqEPrT4X5d5pManD5PTiHCvtpS8uHUWd81U0uljEUhPpNSO7r3ieAkZIrIPWWGjBgViCtLlCPnIHzqEF19U-7mBhjMRRDsVOV5HkXV58bZbV5KCijyDqKSk0nF9-vGrF1HaxYicZgfxcdOwLMrFMtnlYK7JvUcv8g")
            builder.header(RestConst.CONTENT_TYPE, RestConst.CONTENT_TYPE_JSON)
            it.proceed(builder.build())
        }
    }
    factory {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    factory {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL + BuildConfig.API_VERSION)
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get())
            .build()
    }
}
private val providersModule = module {
    single<FuelTypeProvider>()
}

val dataModule = arrayOf(
    repositoriesModule,
    locationModule,
    apiServicesModule,
    networkModule,
    providersModule
)
