package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.VacancyDao

import ru.practicum.android.diploma.data.mapper.VacancyDbMapper

private const val DATABASE_NAME = "app_database.db"

val dataModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
    single<VacancyDao> {
        get<AppDatabase>().getVacancyDao()
    }
    factory { VacancyDbMapper() }
}
