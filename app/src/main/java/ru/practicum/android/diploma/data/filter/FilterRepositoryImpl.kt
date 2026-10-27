package ru.practicum.android.diploma.data.filter

import android.content.Context
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.filters.VacancyFilterDto
import ru.practicum.android.diploma.data.dto.filters.toDomain
import ru.practicum.android.diploma.data.dto.filters.toDto
import ru.practicum.android.diploma.domain.api.FiltersRepository
import ru.practicum.android.diploma.domain.models.filters.VacancyFilter
import ru.practicum.android.diploma.domain.models.filters.isModelEmpty

class FilterRepositoryImpl(private val content: Context, private val gson: Gson) : FiltersRepository {

    private val sharedPrefs = content.getSharedPreferences(
        FILTER_PREFERENCES,
        Context.MODE_PRIVATE
    )

    override fun getFilter(): VacancyFilter {
        val json = sharedPrefs.getString(FILTER_KEY, null)
            ?: return VacancyFilter()

        return gson.fromJson(json, VacancyFilterDto::class.java)?.toDomain() ?: VacancyFilter()
    }

    override fun saveFilter(filter: VacancyFilter) {
        sharedPrefs.edit().apply {
            if (filter.isModelEmpty()) {
                remove(FILTER_KEY)
            } else {
                putString(FILTER_KEY, gson.toJson(filter.toDto()))
            }
        }.apply()
    }

    private companion object {
        const val FILTER_PREFERENCES = "filter_preferences"
        const val FILTER_KEY = "vacancy_filter"
    }
}
