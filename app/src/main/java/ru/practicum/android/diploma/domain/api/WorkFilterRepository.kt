package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.util.Resource

interface WorkFilterRepository {

    fun getCountries(): Flow<Resource<List<FilterArea>>>
    fun getCities(): Flow<Resource<List<FilterArea>>>
    fun getCountryByCity(id: String?): Flow<FilterArea?>
    fun getCitiesByCountry(parentId: String): Flow<Resource<List<FilterArea>>>
}
