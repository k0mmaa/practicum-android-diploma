package ru.practicum.android.diploma.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.JobsApiService


val networkModule = module {


    single<JobsApiService>{
        get<Retrofit>().create(JobsApiService::class.java)
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl( BuildConfig.API_BASE_URL)
            .client(get())
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
    }

    factory<OkHttpClient>{
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
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}
