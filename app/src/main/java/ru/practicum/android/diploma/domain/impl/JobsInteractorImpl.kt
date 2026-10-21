package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.api.JobsRepository
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry
import ru.practicum.android.diploma.domain.models.vacancy.Vacancies
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.util.ErrorType
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

    override fun getVacancies(
        params: Map<String, Any>
    ): Flow<Resource<Vacancies>> {
        return getVacancies(
            area = params["area"] as? Int,
            industry = params["industry"] as? Int,
            text = params["text"] as? String,
            salary = params["salary"] as? Int,
            page = params["page"] as? Int,
            onlyWithSalary = (params["only_with_salary"] as? Boolean)?.takeIf { it }
        )
    }
    override fun getVacancyDetails(
        id: String
    ): Flow<Resource<VacancyDetail>> {
        val vacancyId = id.trim()
        return if (vacancyId.isEmpty()) {
            flowOf(Resource.Error(ErrorType.BAD_REQUEST))
        } else {
            repository.getVacancyDetails(vacancyId)
        }
    }
}
