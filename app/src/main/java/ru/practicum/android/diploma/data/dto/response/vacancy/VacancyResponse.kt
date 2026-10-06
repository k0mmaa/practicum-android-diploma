package ru.practicum.android.diploma.data.dto.response.vacancy

import ru.practicum.android.diploma.data.dto.response.Response

data class VacancyResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyCardDto>
) : Response()
