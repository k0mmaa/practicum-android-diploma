package ru.practicum.android.diploma.filter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FiltersInteractor

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
        filter.value = current.copy(
            onlyWithSalary = value,
        )
    }

    fun save() {
        filter.value?.let {
            filtersInteractor.saveFilter(it)
        }

    }

    fun isFilterWasChanged(): Boolean{
        return filter.value?.isChanged(lastFilter) ?: true
    }
    fun reset(){
        filter.value = VacancyFilter()
    }

    fun setCountry(country: String) {
        val current = filter.value ?: return
        filter.value = current.copy(
            countryId = country,
        )
    }

    fun setRegion(region: String) {
        val current = filter.value ?: return
        filter.value = current.copy(
            regionId = region,
        )
    }

    private fun loadFilter() {
        lastFilter = filtersInteractor.getFilter();
        filter.value = lastFilter.copy()
    }
}
