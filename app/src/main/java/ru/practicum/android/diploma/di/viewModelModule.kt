package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.ui.FavoriteViewModel
import ru.practicum.android.diploma.search.ui.view_model.SearchViewModel
import ru.practicum.android.diploma.vacancy.ui.view_model.VacancyViewModel

val viewModelModule = module {

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        SearchViewModel( get())
    }

    viewModel {
        VacancyViewModel(get() )
    }
}
