package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry
import ru.practicum.android.diploma.domain.models.vacancy.Vacancies
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.util.Resource

interface JobsInteractor {
    fun getAreas(): Flow<Resource<List<FilterArea>>>

    fun getIndustries(): Flow<Resource<List<FilterIndustry>>>

    fun getVacancies(
        area: Int? = null,
        industry: Int? = null,
        text: String? = null,
        salary: Int? = null,
        page: Int? = null,
        onlyWithSalary: Boolean? = null
    ): Flow<Resource<Vacancies>>

    fun getVacancyDetails(
        id: String
    ): Flow<Resource<VacancyDetail>>
}
