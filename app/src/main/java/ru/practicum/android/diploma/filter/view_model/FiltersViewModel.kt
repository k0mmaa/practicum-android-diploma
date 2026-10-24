package ru.practicum.android.diploma.filter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FiltersInteractor
import ru.practicum.android.diploma.domain.api.WorkFilterInteractor
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.filters.FilterOption
import ru.practicum.android.diploma.domain.models.filters.VacancyFilter
import ru.practicum.android.diploma.domain.models.filters.isChanged
import ru.practicum.android.diploma.domain.models.filters.isModelEmpty
import ru.practicum.android.diploma.filter.ui.place.viewModel.CountryFilterState
import ru.practicum.android.diploma.filter.ui.place.viewModel.RegionFilterState
import ru.practicum.android.diploma.util.Debounce
import ru.practicum.android.diploma.util.ErrorType.NOT_FOUND
import ru.practicum.android.diploma.util.Resource


class FiltersViewModel(
    private val filtersInteractor: FiltersInteractor,
    private val workplaceInteractor: WorkFilterInteractor
) : ViewModel() {
    private val filter = MutableLiveData<VacancyFilter>()
    fun observeFilter(): LiveData<VacancyFilter> = filter

    private lateinit var lastFilter: VacancyFilter
    private var latestSearchText: String = ""
    private var searchJob: Job? = null
    private var allCities: List<FilterArea> = emptyList()

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    init {
        loadFilter()
        getCountries()
    }

    private val countriesState = MutableStateFlow<CountryFilterState>(CountryFilterState.Loading)
    val observeCountriesState: LiveData<CountryFilterState> = countriesState.asLiveData()

    private val regionState = MutableStateFlow<RegionFilterState>(RegionFilterState.Start)
    val observeRegionState: LiveData<RegionFilterState> = regionState.asLiveData()

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
        allCities = emptyList()

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

        viewModelScope.launch {
            if (region != null) {
                workplaceInteractor.getCountryByCity(region.id).collect { country ->
                    if (country != null) {
                        val updatedCurrent = filter.value
                        filter.value = updatedCurrent?.copy(country = country.toFilterOption())
                        save()
                    }
                }
            }
        }


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

    private fun setPreviousFilter(){
        val current = filter.value ?: return
        lastFilter = current.copy()
    }
    private fun loadFilter() {
        filter.value =  filtersInteractor.getFilter();
        setPreviousFilter()
    }

    fun clearWorkPlace() {
        val current = filter.value ?: return
        if (current.country == null && current.region == null) return
        allCities = emptyList()
        filter.value = current.copy(country = null, region = null)
        save()
    }

    fun clearIndustry() {
        val current = filter.value ?: return
        if (current.industry == null) return
        filter.value = current.copy(industry = null)
        save()
    }

    fun getCountries() {
        viewModelScope.launch {
            try {
                workplaceInteractor.getCountries().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val countries = resource.data ?: emptyList()
                            if (countries.isEmpty()) {
                                countriesState.value = CountryFilterState.Error
                            } else {
                                countriesState.value = CountryFilterState.Success(countries)
                            }
                        }

                        is Resource.Error -> {
                            countriesState.value = CountryFilterState.Error
                        }
                    }
                }
            } catch (e: Exception) {
                countriesState.value = CountryFilterState.Error
            }
        }
    }

    private val regionSearchDebounce = Debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { changedText ->
        startRegionSearch(changedText)
    }

    fun searchRegionDebounce(changedText: String) {

        if (changedText.isEmpty()) {
            latestSearchText = ""
            regionSearchDebounce.cancel()
            searchJob?.cancel()
            regionState.value = RegionFilterState.Start
            return
        }

        if (allCities.isNotEmpty()) {
            latestSearchText = changedText
            performLocalSearch(changedText)
            return
        }

        regionState.value = RegionFilterState.Loading
        regionSearchDebounce(changedText)
    }

    private fun startRegionSearch(text: String) {
        latestSearchText = text
        if (allCities.isEmpty()) {
            val currentCountryId = filter.value?.country?.id
            searchJob?.cancel()

            searchJob = viewModelScope.launch {
                val citiesFlow = if (currentCountryId != null) {
                    workplaceInteractor.getCitiesByCountry(currentCountryId)
                } else {
                    workplaceInteractor.getCities()
                }

                citiesFlow.collect { result ->
                    if (latestSearchText.isEmpty()) {
                        regionState.value = RegionFilterState.Start
                        return@collect
                    }

                    when (result) {
                        is Resource.Success -> {
                            allCities = result.data ?: emptyList()
                            performLocalSearch(latestSearchText)
                        }

                        is Resource.Error -> {
                            regionState.value = if (result.code == NOT_FOUND) {
                                RegionFilterState.Empty
                            } else {
                                RegionFilterState.Error
                            }
                        }
                    }
                }
            }
        } else {
            performLocalSearch(text)
        }
    }

    private fun performLocalSearch(text: String) {
        val filteredCities = allCities.filter { city ->
            city.name.contains(text, ignoreCase = true)
        }

        if (filteredCities.isEmpty()) {
            regionState.value = RegionFilterState.Empty
        } else {
            regionState.value = RegionFilterState.Content(filteredCities)
        }
    }

}

fun FilterArea.toFilterOption() = FilterOption(
    id = this.id,
    name = this.name
)
