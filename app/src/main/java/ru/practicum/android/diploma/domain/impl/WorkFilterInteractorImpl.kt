package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.WorkFilterInteractor
import ru.practicum.android.diploma.domain.api.WorkFilterRepository
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.util.Resource

class WorkFilterInteractorImpl(private val repository: WorkFilterRepository): WorkFilterInteractor {
    override fun getCountries(): Flow<Resource<List<FilterArea>>> {
        return repository.getCountries()
    }

    override fun getCities(): Flow<Resource<List<FilterArea>>> {
        return  repository.getCities()
    }

    override fun getCountryByCity(id: String?): Flow<FilterArea?> {
        return repository.getCountryByCity(id)
    }

    override fun getCitiesByCountry(parentId: String): Flow<Resource<List<FilterArea>>> {
        return repository.getCitiesByCountry(parentId)
    }

}
