package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.ExternalNavigatorInterface
import ru.practicum.android.diploma.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigatorInterface,
) : SharingInteractor {
    override fun shareLink(shareLink: String) {
        externalNavigator.shareLink(shareLink)
    }

    override fun openEmail(emailTo: String) {
        externalNavigator.openEmail(emailTo)
    }

    override fun openPhone(phone: String) {
        externalNavigator.openPhone(phone)
    }
}
