package ru.practicum.android.diploma.filter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import ru.practicum.android.diploma.domain.models.filters.VacancyFilter


class FiltersViewModel : ViewModel() {
    private val filter = MutableLiveData<VacancyFilter>()
    fun observeFilter(): LiveData<VacancyFilter> = filter

    init {
        loadFilter()
    }


    fun setCountry(country: String){
        val current = filter.value ?: return
        filter.value = current.copy(
            countryId = country,
        )
    }
    fun setRegion(region: String){
        val current = filter.value ?: return
        filter.value = current.copy(
            regionId = region,
        )
    }
    private fun loadFilter(){
        filter.value = VacancyFilter()
    }
}
