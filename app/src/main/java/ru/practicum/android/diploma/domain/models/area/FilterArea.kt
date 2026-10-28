package ru.practicum.android.diploma.domain.models.area

import ru.practicum.android.diploma.domain.models.Identifiable

data class FilterArea(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<FilterArea>
) : Identifiable {
    override fun getIdentifier(): String = id
    override fun hasSameContent(other: Identifiable): Boolean = this == other
}
