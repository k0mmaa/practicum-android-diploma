package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.ui.FavoriteViewModel

val viewModelModule = module{

    viewModel{
        FavoriteViewModel(get())
    }
}
