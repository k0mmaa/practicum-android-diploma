package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.networkModule
import ru.practicum.android.diploma.di.repositoryModule

class App : Application()  {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                interactorModule,
                repositoryModule
                )

        }
    }
}
