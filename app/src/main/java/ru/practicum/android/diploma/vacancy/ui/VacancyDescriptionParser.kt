package ru.practicum.android.diploma.vacancy.ui

import org.jsoup.Jsoup
import java.util.Locale

internal data class VacancyDescriptionParts(
    val introHtml: String,
    val responsibilitiesHtml: String,
    val requirementsHtml: String,
    val termsHtml: String
)

internal class VacancyDescriptionParser {

    private enum class Category {
        RESPONSIBILITIES,
        REQUIREMENTS,
        TERMS
    }

    fun parse(html: String): VacancyDescriptionParts {
        val body = Jsoup.parseBodyFragment(html).body()
        val contentByCategory = Category.values().associateWith { mutableListOf<String>() }

        body.select("section")
            .filter { section -> section.parents().none { it.tagName() == "section" } }
            .forEach { section ->
                val heading = section.selectFirst("h1, h2, h3, h4, h5, h6") ?: return@forEach
                if (section.selectFirst("ul, ol") == null) return@forEach

                val category = categoryFor(heading.text()) ?: return@forEach
                val sectionContent = section.clone()
                sectionContent.selectFirst("h1, h2, h3, h4, h5, h6")?.remove()
                val contentHtml = sectionContent.html().trim()
                if (contentHtml.isNotEmpty()) {
                    contentByCategory.getValue(category).add(contentHtml)
                }
                section.remove()
            }

        return VacancyDescriptionParts(
            introHtml = body.html().trim(),
            responsibilitiesHtml = contentByCategory.getValue(Category.RESPONSIBILITIES)
                .joinToString("\n"),
            requirementsHtml = contentByCategory.getValue(Category.REQUIREMENTS)
                .joinToString("\n"),
            termsHtml = contentByCategory.getValue(Category.TERMS).joinToString("\n")
        )
    }

    private fun categoryFor(heading: String): Category? {
        val normalizedHeading = heading.trim()
            .removeSuffix(":")
            .trim()
            .lowercase(Locale.ROOT)

        return when {
            normalizedHeading == "обязанности" -> Category.RESPONSIBILITIES
            normalizedHeading == "требования" ||
                normalizedHeading.startsWith("требования к ") -> Category.REQUIREMENTS
            normalizedHeading == "условия" ||
                normalizedHeading.startsWith("условия работы") -> Category.TERMS
            else -> null
        }
    }
}
