package ru.practicum.android.diploma.favorites.domain.db

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail

interface FavoriteVacancyInteractor {
    fun favoriteVacancies(): Flow<List<VacancyCard>>
    suspend fun deleteVacancy(id: String)
    suspend fun insertVacancy(vacancy: VacancyDetail)
    fun isVacancyFavorite(id: String): Flow<Boolean>

    suspend fun getVacancy(id: String):VacancyDetail?
}
