package ru.practicum.android.diploma.util

import android.content.res.Resources
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.vacancy.Salary
import java.text.NumberFormat
import java.util.Locale

object SalaryFormatter {

    fun format(
        salary: Salary?,
        resources: Resources,
        somSignSupported: Boolean = false
    ): String {
        val from = salary?.from
        val to = salary?.to

        val numberFormat =
            NumberFormat.getIntegerInstance(Locale("ru", "RU"))

        val amount = when {
            from != null && to != null ->
                "от ${numberFormat.format(from)} до ${numberFormat.format(to)}"

            from != null ->
                "от ${numberFormat.format(from)}"

            to != null ->
                "до ${numberFormat.format(to)}"

            else -> return "Зарплата не указана"
        }

        val symbol = getCurrencySymbol(
            salary?.currency,
            resources,
            somSignSupported
        )

        return if (symbol.isBlank()) amount else "$amount $symbol"
    }

    fun getCurrencySymbol(
        currency: String?,
        resources: Resources,
        somSignSupported: Boolean = false
    ): String {
        val code = currency
            ?.trim()
            ?.uppercase(Locale.ROOT)
            .orEmpty()

        val resourceId = when (code) {
            "RUR", "RUB" -> R.string.currency_rub
            "BYR" -> R.string.currency_byr
            "USD" -> R.string.currency_usd
            "EUR" -> R.string.currency_eur
            "KZT" -> R.string.currency_kzt
            "UAH" -> R.string.currency_uah
            "AZN" -> R.string.currency_azn
            "UZS" -> R.string.currency_uzs
            "GEL" -> R.string.currency_gel
            "KGT" -> if (somSignSupported) {
                R.string.currency_kgt
            } else {
                R.string.currency_kgt_fallback
            }

            else -> return code
        }

        return resources.getString(resourceId)
    }
}
