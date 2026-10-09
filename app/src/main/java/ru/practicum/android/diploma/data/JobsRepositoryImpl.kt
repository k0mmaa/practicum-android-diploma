package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.NetworkClientInterface
import ru.practicum.android.diploma.data.dto.requests.AreasRequest
import ru.practicum.android.diploma.data.dto.requests.IndustriesRequest
import ru.practicum.android.diploma.data.dto.requests.VacanciesRequest
import ru.practicum.android.diploma.data.dto.requests.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.response.area.AreasResponseDto
import ru.practicum.android.diploma.data.dto.response.industries.IndustriesResponseDto
import ru.practicum.android.diploma.data.dto.response.vacancy.VacancyDetailResponse
import ru.practicum.android.diploma.data.dto.response.vacancy.VacancyResponse
import ru.practicum.android.diploma.data.mapper.toModel
import ru.practicum.android.diploma.data.network.NetworkResultCode
import ru.practicum.android.diploma.domain.api.JobsRepository
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry
import ru.practicum.android.diploma.domain.models.vacancy.Vacancies
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource


class JobsRepositoryImpl(
    private val networkClient: NetworkClientInterface
) : JobsRepository {

    override fun getAreas(): Flow<Resource<List<FilterArea>>> = flow {
        val response = networkClient.doRequest(AreasRequest())
        when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                val areas = (response as AreasResponseDto)
                    .areas
                    .map { it.toModel() }

                emit(Resource.Success(areas))
            }
            else -> {
                emit(Resource.Error(mapErrorCode(response.resultCode)))
            }
        }
    }

    override fun getIndustries(): Flow<Resource<List<FilterIndustry>>> = flow {

        val response = networkClient.doRequest(IndustriesRequest())

        when (response.resultCode) {

            NetworkResultCode.SUCCESS -> {
                val industries = (response as IndustriesResponseDto).industries.map { it.toModel() }

                emit(Resource.Success(industries))
            }

            else -> {
                emit(
                    Resource.Error(mapErrorCode(response.resultCode))
                )
            }
        }
    }

    override fun getVacancies(
        area: Int?,
        industry: Int?,
        text: String?,
        salary: Int?,
        page: Int?,
        onlyWithSalary: Boolean?
    ): Flow<Resource<Vacancies>> = flow {

        val response = networkClient.doRequest(
            VacanciesRequest(
                area = area,
                industry = industry,
                text = text,
                salary = salary,
                page = page,
                onlyWithSalary = onlyWithSalary
            )
        )

        when (response.resultCode) {

            NetworkResultCode.SUCCESS -> {

                emit(
                    Resource.Success(
                        (response as VacancyResponse).toModel()
                    )
                )
            }

            else -> {
                emit(
                    Resource.Error(mapErrorCode(response.resultCode))
                )
            }
        }
    }

    override fun getVacancyDetails(
        id: String
    ): Flow<Resource<VacancyDetail>> = flow {

        val response = networkClient.doRequest(
            VacancyDetailsRequest(id)
        )

        when (response.resultCode) {

            NetworkResultCode.SUCCESS -> {
                emit(
                    Resource.Success(
                        (response as VacancyDetailResponse).toModel()
                    )
                )
            }

            else -> {
                emit(Resource.Error(mapErrorCode(response.resultCode)))
            }
        }
    }

    private fun mapErrorCode(code: Int): ErrorType {
        return when (code) {
            NetworkResultCode.NO_INTERNET -> ErrorType.NO_INTERNET
            NetworkResultCode.NOT_FOUND -> ErrorType.NOT_FOUND
            NetworkResultCode.BAD_REQUEST -> ErrorType.BAD_REQUEST
            NetworkResultCode.SERVER_ERROR -> ErrorType.SERVER_ERROR
            else -> ErrorType.SERVER_ERROR
        }
    }
}
