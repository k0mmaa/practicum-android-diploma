package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancyEntity: VacancyEntity)

    @Query("DELETE FROM favorite_vacancies WHERE id = :id")
    suspend fun deleteVacancyById(id: String)

    @Query("SELECT * FROM favorite_vacancies WHERE id = :id")
    suspend fun getVacancy(id: String): VacancyEntity?

    @Query("SELECT * FROM favorite_vacancies ORDER BY addedAt DESC")
    fun getVacancies(): Flow<List<VacancyEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancies WHERE id = :id)")
    fun isVacancyInFavorites(id: String): Flow<Boolean>
}
