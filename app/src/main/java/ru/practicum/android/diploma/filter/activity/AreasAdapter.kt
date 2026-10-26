package ru.practicum.android.diploma.filter.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.search.ui.activity.AbstractListAdapter

class AreasAdapter(clickListener: ClickListener<FilterArea>) : AbstractListAdapter<FilterArea>(
    clickListener = clickListener,
    diffCallback = AreasDiffCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreasViewHolder = AreasViewHolder.from(parent)
}

private class AreasDiffCallback : DiffUtil.ItemCallback<FilterArea>() {

    override fun areItemsTheSame(
        oldItem: FilterArea,
        newItem: FilterArea
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FilterArea,
        newItem: FilterArea
    ): Boolean {
        return oldItem == newItem
    }
}
