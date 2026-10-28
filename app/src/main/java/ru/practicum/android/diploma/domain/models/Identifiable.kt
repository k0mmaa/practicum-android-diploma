package ru.practicum.android.diploma.domain.models

interface Identifiable {

    fun getIdentifier(): String

    fun hasSameContent(other: Identifiable): Boolean
}
