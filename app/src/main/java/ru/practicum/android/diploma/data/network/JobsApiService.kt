package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.response.area.FilterAreaDto
import ru.practicum.android.diploma.data.dto.response.industries.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.response.vacancy.VacancyDetailResponse
import ru.practicum.android.diploma.data.dto.response.vacancy.VacancyResponse

interface JobsApiService {

    // Получить список регионов
    @GET("/areas")
    suspend fun areas(): List<FilterAreaDto>

    // Получить список отраслей.
    @GET("/industries")
    suspend fun industries(): List<FilterIndustryDto>


    @GET("/vacancies")
    suspend fun vacancies(@QueryMap params: Map<String, String>): VacancyResponse

    @GET("/vacancies/{id}")
    suspend fun vacancy(@Path("id") id: String): VacancyDetailResponse
}
