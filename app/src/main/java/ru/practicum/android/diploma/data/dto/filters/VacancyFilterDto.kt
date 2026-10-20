package ru.practicum.android.diploma.data.dto.filters

data class VacancyFilterDto(
    val countryId: String? = null,
    val regionId: String? = null,
    val industryId: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
)
