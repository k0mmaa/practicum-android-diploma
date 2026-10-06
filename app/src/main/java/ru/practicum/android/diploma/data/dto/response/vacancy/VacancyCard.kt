package ru.practicum.android.diploma.data.dto.response.vacancy

data class VacancyCard(
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: Salary?, // зарплата
    val logo: String? // лого компании
)
