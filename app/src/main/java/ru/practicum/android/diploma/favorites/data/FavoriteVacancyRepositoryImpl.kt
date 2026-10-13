package ru.practicum.android.diploma.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.VacancyDao
import ru.practicum.android.diploma.data.mapper.VacancyDbMapper
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyRepository

class FavoriteVacancyRepositoryImpl(
    private val vacancyDao: VacancyDao,
    private val converter: VacancyDbMapper)
    : FavoriteVacancyRepository {

    override fun favoriteVacancies(): Flow<List<VacancyCard>> {
        return vacancyDao.getVacancies().map {
                entities -> converter.mapToCardList(entities)
        }
    }

    override suspend fun deleteVacancy(vacancy: VacancyDetail) {
        vacancyDao.deleteVacancyById(vacancy.id)
    }

    override suspend fun insertVacancy(vacancy: VacancyDetail) {
        vacancyDao.insertVacancy(converter.map(vacancy))
    }
}
