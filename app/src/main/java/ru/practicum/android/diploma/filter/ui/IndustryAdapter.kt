package ru.practicum.android.diploma.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ListItemIndustryBinding
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry

class IndustryAdapter(
    private val onIndustryClick: (FilterIndustry) -> Unit
) : RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder>() {
    private var industries: List<FilterIndustry> = emptyList()
    private var selectedIndustryId: String? = null

    fun update(industries: List<FilterIndustry>, selectedIndustryId: String?) {
        this.industries = industries
        this.selectedIndustryId = selectedIndustryId
        notifyDataSetChanged()
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
        holder.bind(industries[position], industries[position].id == selectedIndustryId)
    }

    override fun getItemCount(): Int = industries.size

    inner class IndustryViewHolder(
        private val binding: ListItemIndustryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(industry: FilterIndustry, isSelected: Boolean) {
            binding.industryName.text = industry.name
            binding.industryRadio.setImageResource(
                if (isSelected) R.drawable.ic_radio_button_on__24px
                else R.drawable.ic_radio_button_off__24px
            )
            binding.root.setOnClickListener { onIndustryClick(industry) }
        }
    }
}
