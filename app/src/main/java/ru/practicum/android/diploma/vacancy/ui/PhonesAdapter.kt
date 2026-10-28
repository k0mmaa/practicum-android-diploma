package ru.practicum.android.diploma.vacancy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyPhoneBinding
import ru.practicum.android.diploma.domain.models.vacancy.Phone


class PhonesAdapter(val clickListener: (Phone) -> Unit) : RecyclerView.Adapter<PhonesViewHolder>() {
    private val phones = mutableListOf<Phone>()

    fun submitList(items: List<Phone>) {
        val updatedPhones = items.toList()
        phones.clear()
        phones.addAll(updatedPhones)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhonesViewHolder {
        val holder = PhonesViewHolder.from(parent)
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                phones.getOrNull(position)?.let(clickListener)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: PhonesViewHolder, position: Int) {
        holder.bind(phones[position])
    }

    override fun getItemCount(): Int = phones.size
}

class PhonesViewHolder(private val binding: ItemVacancyPhoneBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): PhonesViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemVacancyPhoneBinding.inflate(inflater, parent, false)
            return PhonesViewHolder(binding)
        }
    }

    fun bind(phone: Phone) {
        binding.phone.text = phone.formatted
        binding.comment.isVisible = !phone.comment.isNullOrBlank()
        binding.comment.text = binding.root.context.getString(
            R.string.vacancy_phone_comment,
            phone.comment.orEmpty()
        )
    }


}
