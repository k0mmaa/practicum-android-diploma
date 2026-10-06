package ru.practicum.android.diploma.domain.models.vacancy

data class Address(
    val id: String,
    val city: String,
    val street: String,
    val building: String,
    val raw: String
)
