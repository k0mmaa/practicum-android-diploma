package ru.practicum.android.diploma.di

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.NetworkClientInterface
import ru.practicum.android.diploma.data.network.CheckNetwork
import ru.practicum.android.diploma.data.network.CheckNetworkImpl
import ru.practicum.android.diploma.data.network.JobsApiClient
import ru.practicum.android.diploma.data.network.JobsApiService

private const val DEFAULT_API_BASE_URL =
    "https://android-diploma.education-services.ru"
val networkModule = module {
    // Регистрация CheckNetwork
    single<CheckNetwork> {
        CheckNetworkImpl(androidContext())
    }

    single<NetworkClientInterface> {
        JobsApiClient(
            jobsApiService = get(),
            checkNetwork = get()
        )
    }

    single<JobsApiService> {
        get<Retrofit>().create(JobsApiService::class.java)
    }
    single<Retrofit> {
        val baseUrl = BuildConfig.API_BASE_URL.ifBlank {
            DEFAULT_API_BASE_URL
        }
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(get())
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val request = originalRequest.newBuilder().addHeader(
                    "Authorization",
                    "Bearer ${BuildConfig.API_ACCESS_TOKEN}"
                ).addHeader(
                    "Content-Type",
                    "application/json"
                ).build()

                chain.proceed(request)
            }.addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor { message ->
            Log.d("API_HTTP", message)
        }.apply {
            redactHeader("Authorization")
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}
