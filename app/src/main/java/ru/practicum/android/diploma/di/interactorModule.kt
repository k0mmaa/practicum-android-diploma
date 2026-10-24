package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.navigator.ExternalNavigator
import ru.practicum.android.diploma.domain.api.ExternalNavigatorInterface
import ru.practicum.android.diploma.domain.api.FiltersInteractor
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.api.WorkFilterInteractor
import ru.practicum.android.diploma.domain.impl.FiltersInteractorImpl
import ru.practicum.android.diploma.domain.impl.JobsInteractorImpl
import ru.practicum.android.diploma.domain.impl.SharingInteractorImpl
import ru.practicum.android.diploma.domain.impl.WorkFilterInteractorImpl
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteVacancyInteractorImpl

val interactorModule = module {


    factory<JobsInteractor> {
        JobsInteractorImpl(get())
    }

    factory<FavoriteVacancyInteractor> {
        FavoriteVacancyInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<ExternalNavigatorInterface>{
        ExternalNavigator(androidContext())
    }

    factory<FiltersInteractor>{
        FiltersInteractorImpl(get())
    }

    factory<WorkFilterInteractor> {
        WorkFilterInteractorImpl(get())
    }
}
