package ru.practicum.android.diploma.domain.api

interface ExternalNavigatorInterface {

    fun shareLink(link: String)

    fun openEmail(emailTo: String)

    fun openPhone(phone: String)
}
