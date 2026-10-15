package ru.practicum.android.diploma.search.ui.activity

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ListItemVacancyBinding
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.util.SalaryFormatter

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
        val isSvg = item.logo
            ?.let { Uri.parse(it).path }
            ?.endsWith(".svg", ignoreCase = true) == true

        binding.logo.setLayerType(
            if (isSvg) View.LAYER_TYPE_SOFTWARE else View.LAYER_TYPE_NONE,
            null,
        )
        val requestManager = Glide.with(binding.logo)

        val request = if (isSvg) {
            requestManager
                .`as`(PictureDrawable::class.java)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .load(item.logo)
        } else {
            requestManager
                .asDrawable()
                .load(item.logo)
        }

        request
            .placeholder(R.drawable.ic_placeholder_32px)
            .error(R.drawable.ic_placeholder_32px)
            .fallback(R.drawable.ic_placeholder_32px)
            .into(binding.logo)
    }
}
