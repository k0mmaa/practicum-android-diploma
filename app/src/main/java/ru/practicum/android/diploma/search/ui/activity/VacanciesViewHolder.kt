package ru.practicum.android.diploma.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.ListItemVacancyBinding
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.util.SalaryFormatter
import ru.practicum.android.diploma.util.loadLogo

class VacanciesViewHolder(private val binding: ListItemVacancyBinding) : AbstractViewHolder<VacancyCard>(binding.root) {

    companion object {
        fun from(parent: ViewGroup): VacanciesViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemVacancyBinding.inflate(inflater, parent, false)
            return VacanciesViewHolder(binding)
        }
    }

    override fun bind(item: VacancyCard) {
        binding.vacancyName.text = listOfNotNull(item.name, item.city)
            .filter { it.isNotBlank() }
            .joinToString(", ")
        binding.vacancyCompany.text = item.company
        binding.vacancySalary.text = SalaryFormatter.format(
            salary = item.salary,
            resources = binding.root.resources,
            somSignSupported = binding.vacancySalary.paint.hasGlyph("\u20C0")
        )

        binding.logo.loadLogo(item.logo)

    }


}
