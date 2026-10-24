package ru.practicum.android.diploma.filter.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.ListItemCountryBinding
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.search.ui.activity.AbstractViewHolder

class AreasViewHolder(private val binding: ListItemCountryBinding) : AbstractViewHolder<FilterArea>(binding.root) {
    companion object {
        fun from(parent: ViewGroup): AreasViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemCountryBinding.inflate(inflater, parent, false)
            return AreasViewHolder(binding)
        }
    }

    override fun bind(item: FilterArea) {
        binding.countryName.text = item.name
    }
}
