package ru.practicum.android.diploma.data.dto.requests

import ru.practicum.android.diploma.domain.models.filters.VacancyFilter
import ru.practicum.android.diploma.domain.models.filters.isModelEmpty

data class VacanciesRequest(
    val text: String?,
    val page: Int?,
    val filter: VacancyFilter?
) {
    fun toQueryMap(): Map<String, String> {
        val params = mutableMapOf<String, String>()
        text?.let {
            params["text"] = it
        }
        page?.let {
            params["page"] = it.toString()
        }
        if (filter == null || filter.isModelEmpty()) {
            return params
        }

        val areaId = filter.region?.id ?: filter.country?.id
        areaId?.let {
            params["area"] = it
        }

        filter.industry?.id?.let {
            params["industry"] = it
        }

        filter.salary?.takeIf { it > 0 }?.let {
            params["salary"] = it.toString()
        }

        if (filter.onlyWithSalary) {
            params["only_with_salary"] = "true"
        }

        return params
    }
}
