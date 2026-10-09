package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Database(
    entities = [VacancyEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(VacancyDbConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getVacancyDao(): VacancyDao
}
