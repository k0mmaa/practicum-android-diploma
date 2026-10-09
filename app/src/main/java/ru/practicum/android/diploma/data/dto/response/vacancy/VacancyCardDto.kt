package ru.practicum.android.diploma.data.dto.response.vacancy

data class VacancyCardDto(
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: SalaryDto?, // зарплата
    val logo: String? // лого компании
)
