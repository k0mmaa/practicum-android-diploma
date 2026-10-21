package ru.practicum.android.diploma.data.dto.filters

import ru.practicum.android.diploma.domain.models.filters.FilterOption
import ru.practicum.android.diploma.domain.models.filters.VacancyFilter
fun FilterOptionDto.toDomain(): FilterOption {
    return FilterOption(
        id = id,
        name = name
    )
}

fun FilterOption.toDto(): FilterOptionDto {
    return FilterOptionDto(
        id = id,
        name = name
    )
}
fun VacancyFilterDto.toDomain(): VacancyFilter {
    return VacancyFilter(
        country = country?.toDomain(),
        region = region?.toDomain(),
        industry = industry?.toDomain(),
        salary = salary,
        onlyWithSalary = onlyWithSalary
    )
}

fun VacancyFilter.toDto(): VacancyFilterDto {
    return VacancyFilterDto(
        country = country?.toDto(),
        region = region?.toDto(),
        industry = industry?.toDto(),
        salary = salary,
        onlyWithSalary = onlyWithSalary
    )
}
