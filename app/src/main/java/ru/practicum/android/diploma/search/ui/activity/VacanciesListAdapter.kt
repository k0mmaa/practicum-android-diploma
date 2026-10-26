package ru.practicum.android.diploma.search.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard


class VacanciesListAdapter(
    clickListener: ClickListener<VacancyCard>,
) : AbstractListAdapter<VacancyCard>(
    clickListener = clickListener, diffCallback = VacanciesDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacanciesViewHolder = VacanciesViewHolder.from(parent)
}

private class VacanciesDiffCallback : DiffUtil.ItemCallback<VacancyCard>() {

    override fun areItemsTheSame(
        oldItem: VacancyCard,
        newItem: VacancyCard
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: VacancyCard,
        newItem: VacancyCard
    ): Boolean {
        return oldItem == newItem
    }
}
