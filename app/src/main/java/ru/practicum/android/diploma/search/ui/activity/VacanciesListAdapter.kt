package ru.practicum.android.diploma.search.ui.activity

import android.view.ViewGroup
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard

class VacanciesListAdapter(
    clickListener: ClickListener<VacancyCard>
) : AbstractListAdapter<VacancyCard>(clickListener) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacanciesViewHolder = VacanciesViewHolder.from(parent)
}
