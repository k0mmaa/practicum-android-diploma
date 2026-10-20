package ru.practicum.android.diploma.domain.models.filters

data class VacancyFilter(
    val countryId: String? = null,
    val regionId: String? = null,
    val industryId: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
) {
}
