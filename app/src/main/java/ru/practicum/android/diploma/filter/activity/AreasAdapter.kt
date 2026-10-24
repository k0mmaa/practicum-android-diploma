package ru.practicum.android.diploma.filter.activity

import android.view.ViewGroup
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.search.ui.activity.AbstractListAdapter

class AreasAdapter(clickListener: ClickListener<FilterArea>)
    : AbstractListAdapter<FilterArea>(clickListener) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreasViewHolder = AreasViewHolder.from(parent)
}
