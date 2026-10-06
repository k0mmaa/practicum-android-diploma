package ru.practicum.android.diploma.domain.models.vacancy

data class VacancyCard(
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: Salary?,
    val logo: String?
)
