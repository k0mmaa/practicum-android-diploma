package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancies")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val url: String,

    // Зарплата (Salary?) — в три колонки
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,

    // Работодатель (Employer)
    val employerId: String,
    val employerName: String,
    val employerLogo: String?,

    // Регион (FilterArea) и отрасль (FilterIndustry)
    val areaId: String,
    val areaName: String,
    val industryId: String,
    val industryName: String,

    // Опыт, график, занятость (Experience? / Schedule? / Employment?)
    val experienceId: String?,
    val experienceName: String?,
    val scheduleId: String?,
    val scheduleName: String?,
    val employmentId: String?,
    val employmentName: String?,

    // Сложные объекты и списки — хранятся как JSON через VacancyDbConverter - кажется так было в плейлисте
    val address: AddressEntity?,
    val contacts: ContactsEntity?,
    val skills: List<String>,

    // Время добавления вакансии в избранное — для сортировки списка - где-то подсмотрел
    val addedAt: Long,
)
