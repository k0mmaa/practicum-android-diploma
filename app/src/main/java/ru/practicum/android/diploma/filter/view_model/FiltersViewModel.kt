package ru.practicum.android.diploma.filter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FiltersInteractor
import ru.practicum.android.diploma.domain.models.filters.FilterOption
import ru.practicum.android.diploma.domain.models.filters.VacancyFilter
import ru.practicum.android.diploma.domain.models.filters.isChanged
import ru.practicum.android.diploma.domain.models.filters.isModelEmpty


class FiltersViewModel(private val filtersInteractor: FiltersInteractor) : ViewModel() {
    private val filter = MutableLiveData<VacancyFilter>()
    fun observeFilter(): LiveData<VacancyFilter> = filter
    private lateinit var lastFilter: VacancyFilter

    init {
        loadFilter()
    }

    fun setOnlyWithSalary(value: Boolean) {
        val current = filter.value ?: return
        filter.value = current.copy(onlyWithSalary = value)
    }

    fun setSalary(salary: Int?) {
        val current = filter.value ?: return
        filter.value = current.copy(salary = salary)
    }

    fun setCountry(country: FilterOption?) {
        val current = filter.value ?: return
        filter.value = current.copy(
            country = country,
            region = if (current.country?.id == country?.id) {
                current.region
            } else {
                null
            }
        )
    }

    fun setRegion(region: FilterOption?) {
        val current = filter.value ?: return
        filter.value = current.copy(region = region)
    }

    fun setIndustry(industry: FilterOption?) {
        val current = filter.value ?: return
        filter.value = current.copy(industry = industry)
    }

    fun save() {
        val current = filter.value ?: return
        filtersInteractor.saveFilter(current)
        lastFilter = current
    }

    fun isFilterWasChanged(): Boolean {
        return filter.value?.isChanged(lastFilter) ?: false
    }

    fun isFiltersEmpty(): Boolean {
        return filter.value?.isModelEmpty() ?: true
    }

    fun reset() {
        filter.value = VacancyFilter()
    }

    private fun loadFilter() {
        lastFilter = filtersInteractor.getFilter();
        filter.value = lastFilter.copy()
    }
}
