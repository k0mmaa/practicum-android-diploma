package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FiltersInteractor
import ru.practicum.android.diploma.domain.api.FiltersRepository
import ru.practicum.android.diploma.domain.models.filters.VacancyFilter

class FiltersInteractorImpl(private val repository: FiltersRepository): FiltersInteractor {
    override fun getFilter(): VacancyFilter {
        return repository.getFilter()
    }

    override fun saveFilter(filter: VacancyFilter) {
        repository.saveFilter(filter)
    }


}
