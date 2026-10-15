package ru.practicum.android.diploma

import SvgDecoder
import android.app.Application
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.dataModule
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.networkModule
import ru.practicum.android.diploma.di.repositoryModule
import ru.practicum.android.diploma.di.viewModelModule
import java.io.InputStream

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Glide.get(this).registry.append(
            InputStream::class.java,
            PictureDrawable::class.java,
            SvgDecoder(),
        )
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                interactorModule,
                repositoryModule,
                dataModule,
                viewModelModule
            )

        }
    }
}
