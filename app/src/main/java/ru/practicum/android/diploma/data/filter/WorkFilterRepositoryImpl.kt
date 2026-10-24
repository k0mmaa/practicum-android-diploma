package ru.practicum.android.diploma.data.filter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.NetworkClientInterface
import ru.practicum.android.diploma.data.dto.requests.AreasRequest
import ru.practicum.android.diploma.data.dto.response.area.AreasResponseDto
import ru.practicum.android.diploma.data.mapper.toModel
import ru.practicum.android.diploma.data.network.NetworkResultCode
import ru.practicum.android.diploma.domain.api.WorkFilterRepository
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class WorkFilterRepositoryImpl(private val networkClient: NetworkClientInterface): WorkFilterRepository {

    private fun getAreas(): Flow<Resource<List<FilterArea>>> = flow {
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

    override fun getCountries(): Flow<Resource<List<FilterArea>>> {
        return getAreas().map { result ->
            when (result) {
                is Resource.Success -> {
                    val countries = result.data!!.filter { it.parentId == null }
                    Resource.Success(countries)
                }
                is Resource.Error -> {
                    Resource.Error(result.code ?: ErrorType.SERVER_ERROR)
                }
            }
        }
    }

    override fun getCities(): Flow<Resource<List<FilterArea>>> {
        return getAreas().map { result ->
            when (result) {
                is Resource.Success -> {
                    val cities = result.data!!.filter { it.parentId != null }
                    Resource.Success(cities)
                }
                is Resource.Error -> {
                    Resource.Error(result.code ?: ErrorType.SERVER_ERROR)
                }
            }
        }
    }

    override fun getCountryByCity(id: String?): Flow<FilterArea?> {
        return getAreas().map { result ->
            when (result) {
                is Resource.Success -> {
                    val allAreas = result.data ?: emptyList()
                    val city = allAreas.find { it.id == id }

                    if (city != null) findCountry(city, allAreas) else null
                }
                is Resource.Error -> {
                    null
                }
            }
        }
    }

    override fun getCitiesByCountry(parentId: String): Flow<Resource<List<FilterArea>>> {
        return getAreas().map { result ->
            when (result) {
                is Resource.Success -> {
                    val allAreas = result.data ?: emptyList()
                    val country = allAreas.find { it.id == parentId }

                    if (country != null) {
                        val targetCities = mutableListOf<FilterArea>()
                        Resource.Success(targetCities)
                    } else {
                        Resource.Success(emptyList())
                    }
                }
                is Resource.Error -> {
                    Resource.Error(result.code ?: ErrorType.SERVER_ERROR)
                }
            }
        }
    }

    private fun findCountry(current: FilterArea, all: List<FilterArea>): FilterArea? {
        val parent = all.find { it.id == current.parentId } ?: return null
        return findCountry(parent, all)
    }

    private fun mapErrorCode(code: Int): ErrorType {
        return when (code) {
            NetworkResultCode.NO_INTERNET -> ErrorType.NO_INTERNET
            NetworkResultCode.NOT_FOUND -> ErrorType.NOT_FOUND
            NetworkResultCode.BAD_REQUEST -> ErrorType.BAD_REQUEST
            NetworkResultCode.UNAUTHORIZED -> ErrorType.AUTHORIZATION_ERROR
            NetworkResultCode.SERVER_ERROR -> ErrorType.SERVER_ERROR
            else -> ErrorType.SERVER_ERROR
        }
    }

}
