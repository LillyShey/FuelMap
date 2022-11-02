package com.hpk.data.di

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hpk.data.BuildConfig
import com.hpk.data.api.RestConst
import com.hpk.data.providers.FuelTypeProvider
import com.hpk.data.repositories.FuelTypeRepositoryImpl
import com.hpk.data.repositories.LocationRepositoryImpl
import com.hpk.data.services.FuelTypeService
import com.hpk.domain.repositories.FuelTypeRepository
import com.hpk.domain.repositories.LocationRepository
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
import java.util.*

private val repositoriesModule = module {
    single<FuelTypeRepositoryImpl>() bind FuelTypeRepository::class
    single<LocationRepositoryImpl>() bind LocationRepository::class
}
private val locationModule = module {
    single<FusedLocationProviderClient> {
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
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        loggingInterceptor
    }

    factory {
        Interceptor {
            val builder = it.request().newBuilder()
                .url(it.request().url)
            builder.header(RestConst.HEADER_APP_VERSION, BuildConfig.VERSION_NAME)
            builder.header(RestConst.HEADER_PLATFORM, RestConst.CONTENT_PLATFORM)
            builder.header(RestConst.HEADER_ACCEPT_LANGUAGE, Locale.getDefault().language)
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
