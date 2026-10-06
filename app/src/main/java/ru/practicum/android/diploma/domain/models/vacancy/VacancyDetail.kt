package ru.practicum.android.diploma.domain.models.vacancy

import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry

data class VacancyDetail(
    val id: String,
    val name: String,
    val description: String,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry
)
