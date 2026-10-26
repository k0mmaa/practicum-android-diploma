package ru.practicum.android.diploma.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ListItemIndustryBinding
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry

data class IndustryUiItem(
    val industry: FilterIndustry,
    val isSelected: Boolean
)
class IndustryAdapter(
    private val onIndustryClick: (FilterIndustry) -> Unit
) : ListAdapter<IndustryUiItem, IndustryAdapter.IndustryViewHolder>(
    IndustryDiffCallback()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ListItemIndustryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onIndustryClick(item.industry)
        }

    }

    class IndustryViewHolder(
        private val binding: ListItemIndustryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IndustryUiItem) {
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
