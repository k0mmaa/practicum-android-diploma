package ru.practicum.android.diploma.domain.api

interface SharingInteractor {

    fun shareLink(shareLink: String)

    fun openEmail(emailTo: String)

    fun openPhone(phone: String)
}
