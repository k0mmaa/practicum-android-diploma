package ru.practicum.android.diploma.search.ui.view_model

import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard

sealed interface SearchState {
    object Loading : SearchState
    object StartPage : SearchState
    data class Content(
        val vacancies: List<VacancyCard>,
        val totalCount: Int,
    ) : SearchState

    object LoadingNextPage : SearchState
    object NoInternet : SearchState
    object NotFound : SearchState
    object ServerError : SearchState
    object AuthorizationError : SearchState
    data class ErrorWithToast(val errorType: ToastType): SearchState
}

enum class ToastType {
    NO_INTERNET,
    ANYTHING_ELSE
}
