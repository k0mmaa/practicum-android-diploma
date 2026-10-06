package ru.practicum.android.diploma.data.dto.response.vacancy

data class Contacts(
    val id: String,
    val name: String,
    val email: String,
    val phones: List<Phone>
)
