package ru.practicum.android.diploma.filter.ui.place.viewModel

import ru.practicum.android.diploma.domain.models.area.FilterArea

sealed interface CountryFilterState {
    object Loading : CountryFilterState
    data class Success(val countries: List<FilterArea>) : CountryFilterState
    object Error : CountryFilterState
}
