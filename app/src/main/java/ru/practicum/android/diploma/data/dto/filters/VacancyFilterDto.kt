package ru.practicum.android.diploma.data.dto.filters

data class FilterOptionDto(
    val id: String,
    val name: String,
)

data class VacancyFilterDto(
    val country: FilterOptionDto? = null,
    val region: FilterOptionDto? = null,
    val industry: FilterOptionDto? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
)
