package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.filters.VacancyFilter

interface FiltersRepository {

    fun getFilter(): VacancyFilter

    fun saveFilter(filter: VacancyFilter)
}
