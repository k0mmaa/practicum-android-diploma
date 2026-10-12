package com.example.playlistmaker.search.ui.activity


import android.view.ViewGroup


import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.search.ui.activity.AbstractListAdapter

import ru.practicum.android.diploma.search.ui.activity.VacanciesViewHolder


class VacanciesListAdapter(
    clickListener: ClickListener<VacancyCard>
) : AbstractListAdapter<VacancyCard>(clickListener) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacanciesViewHolder = VacanciesViewHolder.from(parent)

}
