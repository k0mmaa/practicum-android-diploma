package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.response.area.FilterAreaDto
import ru.practicum.android.diploma.data.dto.response.industries.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.response.vacancy.AddressDto
import ru.practicum.android.diploma.data.dto.response.vacancy.ContactsDto
import ru.practicum.android.diploma.data.dto.response.vacancy.EmployerDto
import ru.practicum.android.diploma.data.dto.response.vacancy.EmploymentDto
import ru.practicum.android.diploma.data.dto.response.vacancy.ExperienceDto
import ru.practicum.android.diploma.data.dto.response.vacancy.PhoneDto
import ru.practicum.android.diploma.data.dto.response.vacancy.SalaryDto
import ru.practicum.android.diploma.data.dto.response.vacancy.ScheduleDto
import ru.practicum.android.diploma.data.dto.response.vacancy.VacancyCardDto
import ru.practicum.android.diploma.data.dto.response.vacancy.VacancyDetailResponse
import ru.practicum.android.diploma.data.dto.response.vacancy.VacancyResponse
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry
import ru.practicum.android.diploma.domain.models.vacancy.Address
import ru.practicum.android.diploma.domain.models.vacancy.Contacts
import ru.practicum.android.diploma.domain.models.vacancy.Employer
import ru.practicum.android.diploma.domain.models.vacancy.Employment
import ru.practicum.android.diploma.domain.models.vacancy.Experience
import ru.practicum.android.diploma.domain.models.vacancy.Phone
import ru.practicum.android.diploma.domain.models.vacancy.Salary
import ru.practicum.android.diploma.domain.models.vacancy.Schedule
import ru.practicum.android.diploma.domain.models.vacancy.Vacancies
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail

fun FilterAreaDto.toModel(): FilterArea {
    return FilterArea(
        id = id,
        parentId = parentId,
        name = name,
        areas = areas.map { it.toModel() }
    )
}
fun FilterIndustryDto.toModel(): FilterIndustry {
    return FilterIndustry(
        id = id,
        name = name
    )
}
fun SalaryDto.toModel(): Salary {
    return Salary(
        id = id,
        from = from,
        to = to,
        currency = currency
    )
}
fun VacancyCardDto.toModel(): VacancyCard {
    return VacancyCard(
        id = id,
        name = name,
        company = company,
        city = city,
        salary = salary?.toModel(),
        logo = logo
    )
}
fun AddressDto.toModel(): Address {
    return Address(
        id = id,
        city = city,
        street = street,
        building = building,
        raw = raw
    )
}
fun ExperienceDto.toModel(): Experience {
    return Experience(
        id = id,
        name = name
    )
}
fun ScheduleDto.toModel(): Schedule {
    return Schedule(
        id = id,
        name = name
    )
}
fun EmploymentDto.toModel(): Employment {
    return Employment(
        id = id,
        name = name
    )
}
fun PhoneDto.toModel(): Phone {
    return Phone(
        comment = comment,
        formatted = formatted
    )
}
fun ContactsDto.toModel(): Contacts {
    return Contacts(
        id = id,
        name = name,
        email = email,
        phones = phones.map { it.toModel() }
    )
}
fun EmployerDto.toModel(): Employer {
    return Employer(
        id = id,
        name = name,
        logo = logo
    )
}
fun VacancyResponse.toModel(): Vacancies {
    return Vacancies(
        found = found,
        pages = pages,
        page = page,
        items = items.map { it.toModel() }
    )
}
fun VacancyDetailResponse.toModel(): VacancyDetail {
    return VacancyDetail(
        id = id,
        name = name,
        description = description,
        salary = salary?.toModel(),
        address = address?.toModel(),
        experience = experience?.toModel(),
        schedule = schedule?.toModel(),
        employment = employment?.toModel(),
        contacts = contacts?.toModel(),
        employer = employer.toModel(),
        area = area.toModel(),
        skills = skills,
        url = url,
        industry = industry.toModel()
    )
}
