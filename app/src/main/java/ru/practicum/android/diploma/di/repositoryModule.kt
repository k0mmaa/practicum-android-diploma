package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.JobsRepositoryImpl
import ru.practicum.android.diploma.domain.api.JobsRepository

val repositoryModule = module {

    single<JobsRepository> {
        JobsRepositoryImpl(get())
    }
}
