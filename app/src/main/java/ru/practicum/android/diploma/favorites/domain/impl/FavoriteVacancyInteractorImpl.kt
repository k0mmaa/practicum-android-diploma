package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyInteractor
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyRepository

class FavoriteVacancyInteractorImpl(private val repository: FavoriteVacancyRepository) : FavoriteVacancyInteractor {
    override fun favoriteVacancies(): Flow<List<VacancyCard>> {
        return repository.favoriteVacancies()
    }

    override suspend fun deleteVacancy(id: String) {
        repository.deleteVacancy(id)
    }

    override suspend fun insertVacancy(vacancy: VacancyDetail) {
        repository.insertVacancy(vacancy)
    }

    override fun isVacancyFavorite(id: String): Flow<Boolean> {
        return repository.isVacancyFavorite(id)
    }
}
