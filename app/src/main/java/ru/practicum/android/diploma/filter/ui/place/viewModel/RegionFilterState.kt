package ru.practicum.android.diploma.filter.ui.place.viewModel

import ru.practicum.android.diploma.domain.models.area.FilterArea

sealed interface RegionFilterState {
    object Loading: RegionFilterState
    object Error: RegionFilterState
    object Empty: RegionFilterState
    data class Content(val places: List<FilterArea>) : RegionFilterState
}
