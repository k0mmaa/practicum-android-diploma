package ru.practicum.android.diploma.data.dto.response.industries

import ru.practicum.android.diploma.data.dto.response.Response

data class IndustriesResponseDto(
    val industries: List<FilterIndustryDto>
) : Response()
