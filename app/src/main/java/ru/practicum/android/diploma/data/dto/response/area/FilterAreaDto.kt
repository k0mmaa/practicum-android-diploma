package ru.practicum.android.diploma.data.dto.response.area

data class FilterAreaDto(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<FilterAreaDto>
)
