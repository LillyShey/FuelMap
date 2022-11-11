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
            builder.header(RestConst.CONTENT_TYPE, RestConst.CONTENT_TYPE_JSON)
            //FIXME Add token flow
            builder.header(RestConst.HEADER_AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2NjgwNjYyNzEsImV4cCI6MTY2ODE1MjY3MSwicm9sZXMiOlsiUk9MRV9BTk9OWU1PVVMiXSwidXNlcm5hbWUiOiJhbm9ueW1vdXMifQ.b5pkeyk_fPaFnxLe9uJkztnyb44JMgt6rvb5DwpWl6sJtpOXd1wb1Zw2LZSnkXdOV9qZgTZtJ64MTlFWMfo4Mi2FtD0SeMGWukgYFYH5Ld-kt-1ZtMf6c9aq_tL7x01U3llgikWQt0XydkUcjRrJzFAS3XZVRvPx3znsuTgcojaocMstQuqaBbSxe8521_OAX7xECEHj5z_vGDeQy-gR52rYAFBqyypFV0OkWi8OvPZ2Ii5i8F_slNAQuv57asUOkssYuAhB9Zi0v44KOAJI2W6J9ty8ea-PDjh22Qk-eQbZL8NfV9gJ-ErN3fOVlnyrwD7zEqWs8YZ1BWaQghARtw")
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
