package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.impl.JobsInteractorImpl

val interactorModule = module {


    factory<JobsInteractor> {
        JobsInteractorImpl(get())
    }

}
