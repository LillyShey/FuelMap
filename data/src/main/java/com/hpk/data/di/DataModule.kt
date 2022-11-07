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
import com.hpk.data.services.*
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
            builder.header(RestConst.CONTENT_TYPE, RestConst.CONTENT_TYPE_JSON)
            builder.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2Njc4NDYxNDgsImV4cCI6MTY2NzkzMjU0OCwicm9sZXMiOlsiUk9MRV9BTk9OWU1PVVMiXSwidXNlcm5hbWUiOiJhbm9ueW1vdXMifQ.GGmSBA4Z2gGlRe7tZWfUMKwaR3AN-_7KUjIsXWp5onDDgsTzT2QwyXbvidiQ6gRSjiHQ2D6CtvANPbAVCIpYIyszKO93hOaZtRPYguFkGdthFMTzls52_RxgSH5bao6ndvCAMQ8YzwQeLMBfk2pBnuZrq6oey4QpnC7Mk2__akSlOdAbzw3xTl6DzUCm4I1gEEwZ6DVN-IIx9ohCKCVwMn2ueeJUZmmm_8YKfr2tf1EyCQhDvPA4UOVYhNScnRSIpnAub-Myrt9JVeFBlpkcpET5dc_GXhbOxPPa969rqA3xcVzVtoQK3BAg74u99OD4MNWkh-e4BAPxSRmQKqDrlA")
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
