package ru.practicum.android.diploma.data.db.entity

data class ContactsEntity(
    val id: String,
    val name: String,
    val email: String,
    val phones: List<PhoneEntity>
)
