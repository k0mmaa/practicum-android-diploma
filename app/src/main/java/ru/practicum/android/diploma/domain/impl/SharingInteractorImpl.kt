package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.navigator.ExternalNavigator
import ru.practicum.android.diploma.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor  {
    override fun shareLink(shareLink: String) {
        externalNavigator.shareLink(shareLink)
    }
}
