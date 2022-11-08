package com.hpk.data.di

import android.content.Context
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hpk.data.BuildConfig
import com.hpk.data.api.RestConst
import com.hpk.data.api.authentificator.RefreshAuthenticator
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
import org.koin.dsl.factory
import org.koin.dsl.module
import org.koin.dsl.single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val repositoriesModule = module {
    single<FuelTypeRepositoryImpl>() bind FuelTypeRepository::class
    single<LocationRepositoryImpl>() bind LocationRepository::class
    single<AuthRepositoryImpl>() bind AuthRepository::class

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
    single<FuelTypeService> {
        (get() as Retrofit).create(FuelTypeService::class.java)
    }
    single<AuthService> {
        (get() as Retrofit).create(AuthService::class.java)
    }
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
            get<TokenProvider>().provideAccessToken()?.let { accessToken ->
                Log.i("Access token", accessToken.token)
                builder.header(RestConst.HEADER_AUTHORIZATION, "Bearer ${accessToken.token}")
            }
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
    factory<RefreshAuthenticator>()

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
