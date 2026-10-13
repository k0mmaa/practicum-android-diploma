package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.impl.JobsInteractorImpl
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteVacancyInteractorImpl

val interactorModule = module {


    factory<JobsInteractor> {
        JobsInteractorImpl(get())
    }

    single<FavoriteVacancyInteractor> {
        FavoriteVacancyInteractorImpl(get())
    }
}
