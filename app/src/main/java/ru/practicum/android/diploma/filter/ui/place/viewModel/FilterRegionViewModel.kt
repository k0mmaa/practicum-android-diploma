package ru.practicum.android.diploma.filter.ui.place.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.util.Resource

class FilterRegionViewModel(private val interactor: JobsInteractor) : ViewModel() {

    private var query: String = ""

    private var loadJob: Job? = null
    private var areas: List<FilterArea> = emptyList()
    private var allRegions: List<FilterArea> = emptyList()
    private val regionState = MutableStateFlow<RegionFilterState>(RegionFilterState.Loading)
    val observeRegionState: LiveData<RegionFilterState> = regionState.asLiveData()

    fun loadRegion(parentId: String?) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            regionState.value = RegionFilterState.Loading
            interactor.getAreas().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        areas = resource.data ?: emptyList()
                        allRegions = getRegions(areas, parentId)
                        if (allRegions.isEmpty()) {
                            regionState.value = RegionFilterState.Empty
                        } else {
                            regionState.value = RegionFilterState.Content(allRegions)
                        }
                    }

                    is Resource.Error -> {
                        regionState.value = RegionFilterState.Error
                    }
                }
            }
        }
    }

    fun getRegions(areas: List<FilterArea>, countryId: String? = null): List<FilterArea> {
        val result = mutableListOf<FilterArea>()

        fun extractAreas(currentArea: FilterArea) {
            for (subArea in currentArea.areas) {
                result.add(subArea)
                extractAreas(subArea)
            }
        }

        val filteredCountries = areas.filter { country ->
            countryId == null || country.id == countryId
        }
        for (country in filteredCountries) {
            extractAreas(country)
        }
        return result
    }

    fun getCountryByCity(current: FilterArea): FilterArea? {
        if (current.parentId == null) return null

        fun findCountry(rootList: List<FilterArea>, parentId: String?): FilterArea? {
            for (area in rootList) {
                if (area.id == parentId) return area
            }
            return null
        }
        var currentParent = findCountry(areas, current.parentId)
        return currentParent
    }


    fun searchRegion(value: String) {
        query = value.trim()
        val filteredRegion = allRegions.filter { it.name.startsWith(query, ignoreCase = true) }

        if (filteredRegion.isEmpty()) {
            regionState.value = RegionFilterState.Empty
        } else {
            regionState.value = RegionFilterState.Loading
            regionState.value = RegionFilterState.Content(filteredRegion)
        }
    }
}
