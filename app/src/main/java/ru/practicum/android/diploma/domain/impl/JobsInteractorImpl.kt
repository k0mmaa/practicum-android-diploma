package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.api.JobsRepository
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry
import ru.practicum.android.diploma.domain.models.vacancy.Vacancies
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.util.Resource

class JobsInteractorImpl(
    private val repository: JobsRepository
) : JobsInteractor {

    override fun getAreas(): Flow<Resource<List<FilterArea>>> {
        return repository.getAreas()
    }

    override fun getIndustries(): Flow<Resource<List<FilterIndustry>>> {
        return repository.getIndustries()
    }

    override fun getVacancies(
        area: Int?,
        industry: Int?,
        text: String?,
        salary: Int?,
        page: Int?,
        onlyWithSalary: Boolean?
    ): Flow<Resource<Vacancies>> {
        return repository.getVacancies(
            area = area,
            industry = industry,
            text = text,
            salary = salary,
            page = page,
            onlyWithSalary = onlyWithSalary
        )
    }

    override fun getVacancyDetails(
        id: String
    ): Flow<Resource<VacancyDetail>> {
        return repository.getVacancyDetails(id)
    }
}
