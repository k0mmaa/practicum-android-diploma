package ru.practicum.android.diploma.data.dto.response.vacancy

import ru.practicum.android.diploma.data.dto.response.Response
import ru.practicum.android.diploma.data.dto.response.area.FilterAreaDto
import ru.practicum.android.diploma.data.dto.response.industries.FilterIndustryDto

data class VacancyDetailResponse(
    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryDto?, // зарплата
    val address: AddressDto?,
    val experience: ExperienceDto?, // опыт
    val schedule: ScheduleDto?, // рабочий день (полный/ не полный)
    val employment: EmploymentDto?,
    val contacts: ContactsDto?,
    val employer: EmployerDto, // компания и ее лого
    val area: FilterAreaDto,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustryDto
) : Response()
