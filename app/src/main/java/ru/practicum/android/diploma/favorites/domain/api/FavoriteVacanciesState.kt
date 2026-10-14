package ru.practicum.android.diploma.favorites.domain.api

import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard

sealed interface FavoriteVacanciesState {
    object Error : FavoriteVacanciesState
    object Empty : FavoriteVacanciesState
    data class Content(val vacanciesList: List<VacancyCard>) : FavoriteVacanciesState

}
