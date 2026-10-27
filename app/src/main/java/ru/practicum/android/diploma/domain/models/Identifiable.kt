package ru.practicum.android.diploma.domain.models

interface Identifiable {

    fun getIdentifier(): String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
