package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.ui.FavoriteViewModel
import ru.practicum.android.diploma.filter.view_model.IndustryViewModel
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel
import ru.practicum.android.diploma.search.ui.view_model.SearchViewModel
import ru.practicum.android.diploma.vacancy.ui.VacancyViewModel

val viewModelModule = module {

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        SearchViewModel( get(), get())
    }

    viewModel {
        VacancyViewModel(get(), get(), get())
    }
    viewModel { FiltersViewModel(get()) }
    viewModel { IndustryViewModel(get()) }
}
