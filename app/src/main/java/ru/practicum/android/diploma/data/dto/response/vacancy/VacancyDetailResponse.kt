package ru.practicum.android.diploma.data.dto.response.vacancy

import ru.practicum.android.diploma.data.dto.response.Response
import ru.practicum.android.diploma.data.dto.response.area.FilterArea
import ru.practicum.android.diploma.data.dto.response.industries.FilterIndustry

data class VacancyDetailResponse(
    val id: String,
    val name: String,
    val description: String,
    val salary: Salary?, // зарплата
    val address: Address?,
    val experience: Experience?, // опыт
    val schedule: Schedule?, // рабочий день (полный/ не полный)
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer, // компания и ее лого
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry
) : Response()
