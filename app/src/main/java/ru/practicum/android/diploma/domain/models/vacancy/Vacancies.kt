package ru.practicum.android.diploma.domain.models.vacancy

data class Vacancies(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyCard>
)
