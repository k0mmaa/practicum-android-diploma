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

private const val DEFAULT_API_BASE_URL = "https://android-diploma.education-services.ru"
private const val AUTHORIZATION_SCHEME = "Bearer"

val networkModule = module {
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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory<OkHttpClient> {
        val token = BuildConfig.API_ACCESS_TOKEN.trim()
        val authorization = when {
            token.isBlank() -> null
            token.startsWith("Bearer ", ignoreCase = true) -> token
            else -> listOf(AUTHORIZATION_SCHEME, token).joinToString(" ")
        }

        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .apply {
                        authorization?.let { header("Authorization", it) }
                    }
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(get<HttpLoggingInterceptor>())
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
