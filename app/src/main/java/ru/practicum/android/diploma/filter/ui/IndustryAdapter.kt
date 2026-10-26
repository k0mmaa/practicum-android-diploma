package ru.practicum.android.diploma.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ListItemIndustryBinding
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry
import ru.practicum.android.diploma.search.ui.activity.AbstractListAdapter
import ru.practicum.android.diploma.search.ui.activity.AbstractViewHolder

data class IndustryUiItem(
    val industry: FilterIndustry,
    val isSelected: Boolean
)

class IndustryAdapter(
    onIndustryClick: ClickListener<IndustryUiItem>
) : AbstractListAdapter<IndustryUiItem>(
    clickListener = onIndustryClick,
    diffCallback = IndustryDiffCallback()
) {

    fun update(industries: List<FilterIndustry>, selectedIndustryId: String?) {
        val items = industries.map { industry ->
            IndustryUiItem(
                industry = industry,
                isSelected = industry.id == selectedIndustryId
            )
        }
        submitList(items)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndustryViewHolder = IndustryViewHolder.from(parent)


    class IndustryViewHolder(
        private val binding: ListItemIndustryBinding
    ) : AbstractViewHolder<IndustryUiItem>(binding.root) {

        companion object {
            fun from(parent: ViewGroup): IndustryViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemIndustryBinding.inflate(inflater, parent, false)
                return IndustryViewHolder(binding)
            }
        }

        override fun bind(item: IndustryUiItem) {
            binding.industryName.text = item.industry.name
            binding.industryRadio.setImageResource(
                if (item.isSelected) R.drawable.ic_radio_button_on__24px
                else R.drawable.ic_radio_button_off__24px
            )
        }
    }
}

private class IndustryDiffCallback : DiffUtil.ItemCallback<IndustryUiItem>() {

    override fun areItemsTheSame(
        oldItem: IndustryUiItem,
        newItem: IndustryUiItem
    ): Boolean {
        return oldItem.industry.id == newItem.industry.id
    }

    override fun areContentsTheSame(
        oldItem: IndustryUiItem,
        newItem: IndustryUiItem
    ): Boolean {
        return oldItem == newItem
    }
}
