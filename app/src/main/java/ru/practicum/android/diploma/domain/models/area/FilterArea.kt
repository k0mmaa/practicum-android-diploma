package ru.practicum.android.diploma.domain.models.area

data class FilterArea(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<FilterArea>
)
