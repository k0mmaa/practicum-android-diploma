package ru.practicum.android.diploma.search.ui.view_model

import ru.practicum.android.diploma.domain.models.vacancy.Vacancies

sealed interface SearchState {
    object Loading : SearchState
    object StartPage : SearchState
    data class Content(
        val vacancies: Vacancies
    ) : SearchState

    object NoInternet : SearchState
    object NotFound : SearchState
}
