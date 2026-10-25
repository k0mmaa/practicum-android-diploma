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
        if (current.onlyWithSalary == value) return
        filter.value = current.copy(onlyWithSalary = value)
        save()
    }

    fun setSalary(salary: Int?) {
        val current = filter.value ?: return
        if (current.salary == salary) return
        filter.value = current.copy(salary = salary)
        save()
    }

    fun setCountry(country: FilterOption?) {
        val current = filter.value ?: return
        if (current.country == country) return
        filter.value = current.copy(
            country = country,
            region = if (current.country?.id == country?.id) {
                current.region
            } else {
                null
            }
        )
        save()
    }

    fun setRegion(region: FilterOption?) {
        val current = filter.value ?: return
        if (current.region == region) return
        filter.value = current.copy(region = region)
        save()
    }

    fun setIndustry(industry: FilterOption?) {
        val current = filter.value ?: return
        if (current.industry == industry) return
        filter.value = current.copy(industry = industry)
        save()
    }

    fun isFilterWasChanged(): Boolean {
        return filter.value?.isChanged(lastFilter) ?: false
    }

    fun save() {
        val current = filter.value ?: return
        filtersInteractor.saveFilter(current)
    }


    fun isFiltersEmpty(): Boolean {
        return filter.value?.isModelEmpty() ?: true
    }

    fun reset() {
        filter.value = VacancyFilter()
        save()
    }

    private fun setPreviousFilter() {
        val current = filter.value ?: return
        lastFilter = current.copy()
    }

    private fun loadFilter() {
        filter.value = filtersInteractor.getFilter()
        setPreviousFilter()
    }

    fun clearWorkPlace() {
        val current = filter.value ?: return
        if (current.country == null && current.region == null) return
        filter.value = current.copy(country = null, region = null)
        save()
    }

    fun clearRegion() {
        val current = filter.value ?: return
        if (current.region == null) return
        filter.value = current.copy(region = null)
        save()
    }

    fun clearCountry() {
        val current = filter.value ?: return
        if (current.country == null) return
        filter.value = current.copy(country = null)
        save()
    }

    fun clearIndustry() {
        val current = filter.value ?: return
        if (current.industry == null) return
        filter.value = current.copy(industry = null)
        save()
    }
}
