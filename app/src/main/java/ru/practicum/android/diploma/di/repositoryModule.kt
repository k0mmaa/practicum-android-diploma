package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.JobsRepositoryImpl
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
}
