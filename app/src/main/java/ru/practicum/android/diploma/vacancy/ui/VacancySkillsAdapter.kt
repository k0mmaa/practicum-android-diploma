package ru.practicum.android.diploma.vacancy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancySkillBinding

class VacancySkillsAdapter : RecyclerView.Adapter<VacancySkillsAdapter.SkillViewHolder>() {
    private val skills = mutableListOf<String>()

    fun submitList(items: List<String>) {
        skills.clear()
        skills.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val binding = ItemVacancySkillBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SkillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        holder.bind(skills[position])
    }

    override fun getItemCount(): Int = skills.size

    class SkillViewHolder(
        private val binding: ItemVacancySkillBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(skill: String) {
            binding.skillName.text = skill
        }
    }
}
