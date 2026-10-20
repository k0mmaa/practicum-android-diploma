package ru.practicum.android.diploma.data.dto.filters

import ru.practicum.android.diploma.domain.models.filters.VacancyFilter

fun VacancyFilterDto.toDomain(): VacancyFilter {
    return VacancyFilter(
        countryId = countryId,
        regionId = regionId,
        industryId = industryId,
        salary = salary,
        onlyWithSalary = onlyWithSalary
    )
}

fun VacancyFilter.toDto(): VacancyFilterDto {
    return VacancyFilterDto(
        countryId = countryId,
        regionId = regionId,
        industryId = industryId,
        salary = salary,
        onlyWithSalary = onlyWithSalary
    )
}
