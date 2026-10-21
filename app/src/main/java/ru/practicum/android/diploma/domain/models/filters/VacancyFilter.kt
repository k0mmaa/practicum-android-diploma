package ru.practicum.android.diploma.domain.models.filters

data class FilterOption(
    val id: String,
    val name: String,
)

data class VacancyFilter(
    val country: FilterOption? = null,
    val region: FilterOption? = null,
    val industry: FilterOption? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
)

fun VacancyFilter.isModelEmpty(): Boolean {
    return country == null
        && region == null
        && industry == null
        && salary == null
        && !onlyWithSalary
}
fun VacancyFilter.isChanged(other: VacancyFilter): Boolean {
    return this != other
}
