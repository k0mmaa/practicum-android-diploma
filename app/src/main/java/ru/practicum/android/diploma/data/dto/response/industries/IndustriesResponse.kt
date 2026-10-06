package ru.practicum.android.diploma.data.dto.response.industries

import ru.practicum.android.diploma.data.dto.response.Response

data class IndustriesResponse(
    val industries: List<FilterIndustry>
) : Response()
