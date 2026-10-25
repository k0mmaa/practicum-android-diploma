package ru.practicum.android.diploma.di

import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.JobsRepositoryImpl
import ru.practicum.android.diploma.data.filter.FilterRepositoryImpl
import ru.practicum.android.diploma.domain.api.FiltersRepository
import ru.practicum.android.diploma.domain.api.JobsRepository
import ru.practicum.android.diploma.favorites.data.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyRepository

val repositoryModule = module {

    single<JobsRepository> {
        JobsRepositoryImpl(get())
    }

    single<FavoriteVacancyRepository> {
        FavoriteVacancyRepositoryImpl(get(), get())
    }

    single<FiltersRepository> {
        FilterRepositoryImpl(content = androidContext(), gson = get())
    }


    single { Gson() }
}
