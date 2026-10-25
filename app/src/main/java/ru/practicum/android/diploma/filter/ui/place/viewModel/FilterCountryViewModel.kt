package ru.practicum.android.diploma.filter.ui.place.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.util.Resource

class FilterCountryViewModel(private val interactor: JobsInteractor) : ViewModel() {

    init {
        loadCountries()
    }

    private val countriesState = MutableStateFlow<CountryFilterState>(CountryFilterState.Loading)
    val observeCountriesState: LiveData<CountryFilterState> = countriesState.asLiveData()

    fun loadCountries() {
        viewModelScope.launch {
            try {
                interactor.getAreas().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val areas = resource.data ?: emptyList()
                            val countries = getCountries(areas)
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
}

fun getCountries(areas: List<FilterArea>): List<FilterArea> {
    return areas
        .filter { it.parentId == null }
}
